"""
Implementation of attack methods. Running this file as a program will
apply the attack to the model specified by the config file and store
the examples in an .npy file.
"""
from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import tensorflow as tf
import TheModel as tm
import random
import csv
import numpy as np

num_of_classes = 2
time_steps = 140
num_of_features = 1

csv_file_path = "/Users/ekin/Documents/Adverserial_Time_Series/Models/PGDModel/"

class DataPipe:
    def __init__(self, csv_path):
        # Reading data from .tsv files
        tsv_file = open(csv_path)
        file_tsv_reader = csv.reader(tsv_file, delimiter="\t")

        self._file_tsv_reader = file_tsv_reader
        self._i = 0

        self._input_time_series, self._output_labels = self.loadData()
        print("Data has been loaded")

    def loadData(self):
        # Loading data
        print("Loading data...")
        tmp_input_time_series, tmp_output_labels = self.read_data(self._file_tsv_reader)
        return tmp_input_time_series, tmp_output_labels

    def read_data(self, tsv_reader):
        data_X = []
        data_y = []
        count = 0
        for row in tsv_reader:
            count += 1
            data_X = data_X + [float(e) for e in row[1:]]
            data_y.append(int(float(row[0])))

        print("Number of rows in DATA: " + str(count))

        # Finalizing data for X
        data_X = np.array(data_X)
        data_X = data_X.reshape((-1, time_steps, num_of_features))

        # Now one hot encoding data Y and finalizing
        one_hot_data_y = []
        for e in data_y:
            one_hot = [0] * num_of_classes
            # Labels are starting from 1, making it 0
            e = e - 1
            one_hot[e] = 1
            one_hot_data_y.append(one_hot)

        data_y = np.array(one_hot_data_y)
        data_y = data_y.reshape((-1, num_of_classes))

        return data_X, data_y

    def next_batch(self, batch_size):
        if self._i + batch_size > len(self._input_time_series):
            self._i = 0
        x, y = self._input_time_series[self._i:self._i + batch_size], self._output_labels[self._i:self._i + batch_size]
        self._i = self._i + batch_size

        return x, y


class LinfPGDAttack:
    def __init__(self, model, epsilon, k, a, random_start, loss_func):
        """Attack parameter initialization. The attack performs k steps of
       size a, while always staying within epsilon from the initial
       point."""
        self.model = model
        self.epsilon = epsilon
        self.k = k
        self.a = a
        self.rand = random_start

        if loss_func == 'xent':
            loss = model.xent
        elif loss_func == 'cw':
            """label_mask = tf.one_hot(model.labels,
                                    2,  # number of classes
                                    on_value=1.0,
                                    off_value=0.0,
                                    dtype=tf.float32)"""
            correct_logit = tf.reduce_sum(model.labels * model.final_output, axis=1)
            wrong_logit = tf.reduce_max((1 - model.labels ) * model.final_output - 1e4 * model.labels , axis=1)
            loss = -tf.nn.relu(correct_logit - wrong_logit + 50)
        else:
            print('Unknown loss function. Defaulting to cross-entropy')
            loss = model.xent

        self.grad = tf.gradients(loss, model.inputs)[0]

    def perturb(self, x_nat, y, sess):
        """Given a set of examples (x_nat, y), returns a set of adversarial
       examples within epsilon of x_nat in l_infinity norm."""
        if self.rand:
            x = x_nat + np.random.uniform(-self.epsilon, self.epsilon, x_nat.shape)
            # x = np.clip(x, 0, 1) # we don't need to ensure this. do we ?
        else:
            x = np.copy(x_nat)

        for i in range(self.k):
            grad = sess.run(self.grad, feed_dict={self.model.inputs: x,
                                                  self.model.labels: y
                                                  })

            x += self.a * np.sign(grad)

            x = np.clip(x, x_nat - self.epsilon, x_nat + self.epsilon)
            # x = np.clip(x, 0, 1) # we don't need to ensure this. do we ?

        return x


if __name__ == '__main__':
    import json
    import sys
    import math

    with open("/Users/ekin/Documents/Adverserial_Time_Series/Models/PGDModel/config.json") as config_file:
        config = json.load(config_file)
    model_file = tf.train.latest_checkpoint(config['model_dir'])
    if model_file is None:
        print('No model found')
        sys.exit()
    model = tm.TheModel()  # Model is created

    attack = LinfPGDAttack(model,
                           config['epsilon'],
                           config['k'],
                           config['a'],
                           config['random_start'],
                           config['loss_func'])

    saver = tf.train.Saver()

    #mnist = input_data.read_data_sets('MNIST_data', one_hot=False)  # why false ?
    dp = DataPipe(csv_path=csv_file_path)

    with tf.Session() as sess:
        # Restore the checkpoint
        saver.restore(sess, model_file)

        # Iterate over the samples batch-by-batch
        num_eval_examples = config['num_eval_examples']
        eval_batch_size = config['eval_batch_size']
        num_batches = int(math.ceil(num_eval_examples / eval_batch_size))

        x_adv = []  # adv accumulator

        print('Iterating over {} batches'.format(num_batches))

        count = 0
        for ibatch in range(num_batches):
          x_batch, y_batch = dp.next_batch(batch_size=config["eval_batch_size"])
          x_batch_adv = attack.perturb(x_batch, y_batch, sess)
          count+= 1
          print(count," th bath has iterated")
          x_adv.append(x_batch_adv)

        print('Storing examples')
        path = config['store_adv_path']
        x_adv = np.concatenate(x_adv, axis=0)
        np.save(path, x_adv)
        print('Examples stored in {}'.format(path))

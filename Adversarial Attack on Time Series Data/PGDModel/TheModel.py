import tensorflow as tf
import os
import random

# Preventing error
drive_name = ""
os.environ['KMP_DUPLICATE_LIB_OK'] = 'True'

random.seed(20)

# Hyperparameters:
batch_size = 16
hidden_layer_size = 8
num_LSTM_layers = 1

# Data spesific:
num_of_classes = 2
num_of_features = 1
time_steps = 140


class TheModel(object):
    def __init__(self):
        #tf.compat.v1.disable_eager_execution()
        self.inputs = tf.placeholder(dtype=tf.float32, shape=[batch_size, time_steps, num_of_features])
        self.labels = tf.placeholder(dtype=tf.float32, shape=[batch_size, num_of_classes])

        with tf.variable_scope("lstm"):
            # cells = tf.contrib.rnn.MultiRNNCell(cells=[self.lstm_cell() for _ in range(num_LSTM_layers)],
            #                                    state_is_tuple=True)
            outputs, states = tf.nn.dynamic_rnn(self.lstm_cell(), self.inputs,
                                                dtype=tf.float32)
            weights = {
                'linear_layer': tf.Variable(tf.truncated_normal([hidden_layer_size,
                                                                 num_of_classes],
                                                                mean=0, stddev=.01))
            }
            biases = {
                'linear_layer': tf.Variable(tf.truncated_normal([num_of_classes],
                                                                mean=0, stddev=.01))
            }

        # Extract the last relevant output and use in a linear layer
        self.final_output = tf.matmul(states[1], weights["linear_layer"]) + biases["linear_layer"]

        self.softmax = tf.nn.softmax_cross_entropy_with_logits(logits=self.final_output,
                                                               labels=self.labels)
        self.cross_entropy = tf.reduce_mean(self.softmax)

        self.train_step = tf.train.RMSPropOptimizer(0.001, 0.9).minimize(self.cross_entropy)
        correct_prediction = tf.equal(tf.argmax(self.labels, 1),
                                      tf.argmax(self.final_output, 1))

        self.accuracy = (tf.reduce_mean(tf.cast(correct_prediction,
                                                tf.float32))) * 100

    def lstm_cell(self):
        lstm = tf.contrib.rnn.BasicLSTMCell(hidden_layer_size, forget_bias=1.0)
        return lstm








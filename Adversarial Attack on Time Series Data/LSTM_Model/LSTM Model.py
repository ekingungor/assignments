import tensorflow.compat.v1 as tf
import numpy as np
import os
import json
tf.disable_v2_behavior()
tf.reset_default_graph()

os.environ['KMP_DUPLICATE_LIB_OK'] = 'True'


class DataPipe:
    def __init__(self, train_npy_path, test_npy_path):
        self._train_i = 0
        self._test_i = 0
        print("Reading Training Data...")
        _train_data = np.load(train_npy_path)
        _train_data = _train_data.astype("float32")
        _train_input_data = []
        _train_label_data = []

        if shuffle_:
            np.random.shuffle(_train_data)

        for row in _train_data:
            _train_label_data.append(row[0])
            _train_input_data.append([row[1:]])

        _train_input_data = np.array(_train_input_data).reshape((-1, time_steps,num_of_features))
        self.train_input_data = _train_input_data

        _train_label_data = np.array(_train_label_data).astype("int32")
        self.train_label_data = self.one_hot(_train_label_data)

        print(_train_data.shape)
        print("Training Data has been read\n")

        print("Reading Training Data...")
        _test_data = np.load(test_npy_path)
        _test_data = _test_data.astype("float32")
        _test_input_data = []
        _test_label_data = []

        if shuffle_:
            np.random.shuffle(_test_data)

        for row in _test_data:
            _test_label_data.append(row[0])
            _test_input_data.append([row[1:]])

        _test_input_data = np.array(_test_input_data).reshape((-1, time_steps,num_of_features))
        self.test_input_data = _test_input_data

        _test_label_data = np.array(_test_label_data).astype("int32")
        self.test_label_data = self.one_hot(_test_label_data)

        print(_test_data.shape)
        print("Test Data has been read\n")

    def one_hot(self,data):
        one_hot_data = []
        for e in data:
            one_hot = [0] * num_of_classes
            e = e - 1
            one_hot[e] = 1
            one_hot_data.append(one_hot)
        return np.array(one_hot_data).reshape(-1,num_of_classes)


    def next_train_batch(self, batch_size):
        if self._train_i + batch_size > len(self.train_input_data):
            rest = batch_size - (len(self.train_input_data) - self._train_i)

            a = self.train_input_data[self._train_i:len(self.train_input_data)]
            b = self.train_input_data[0:rest]
            x = np.concatenate((a, b), axis=0)


            c = self.train_label_data[self._train_i:len(self.train_label_data)]
            d = self.train_label_data[0:rest]
            y = np.concatenate((c, d), axis=0)
            self._train_i = rest
        else:
            x, y = self.train_input_data[self._train_i:self._train_i + batch_size], self.train_label_data[
                                                                                    self._train_i:self._train_i + batch_size]
            self._train_i = self._train_i + batch_size

        return x, y

    def next_test_batch(self, batch_size):
        if self._test_i + batch_size > len(self.test_input_data):
            self._test_i = 0

        x, y = self.test_input_data[self._test_i:self._test_i + batch_size], self.test_label_data[
                                                                               self._test_i:self._test_i + batch_size]
        self._test_i = (self._test_i + batch_size)
        return x, y


def test(sess):
    total_accuracy = 0
    many_times = len(dp.test_input_data) // batch_size
    _X = []
    _y = []
    for i in range(many_times):
        x_tmp, y_tmp = dp.next_test_batch(batch_size)
        _X.append(x_tmp)
        _y.append(y_tmp)
    X = np.array(_X).reshape(many_times, batch_size, time_steps, num_of_features)
    Y = np.array(_y).reshape(many_times, batch_size, num_of_classes)

    acc = [sess.run(accuracy, feed_dict={inputs: X[i],
                                         labels: Y[i]}) for i in range(many_times)]

    for i in range(len(acc)):
        total_accuracy += acc[i] * batch_size / (batch_size * many_times)
        print("Accuracy of Test %d: %.5f" % (i + 1, acc[i]))

    print("Total accuracy is", total_accuracy)


def train(sess):
    sess.run(tf.global_variables_initializer())
    saver = tf.train.Saver(max_to_keep=config["max_to_keep"])
    for step in range(config["steps"]):
        # Intermediate testings
        x_batch, y_batch = dp.next_train_batch(batch_size)
        # print(x_batch.reshape(-1,time_steps),y_batch.reshape(-1,num_of_classes))

        if step % config["iterations_per_accuracy_test"] == 0:
            acc, loss = sess.run([accuracy, cross_entropy], feed_dict={inputs: x_batch,
                                                                       labels: y_batch})
            print("Accuracy and loss at %d: %.5f , %.5f" % (step, acc, loss))

        if step % config["iterations_per_cross_validaton_test"] == 0:
            x_test_batch, y_test_batch = dp.next_test_batch(batch_size)
            acc, loss = sess.run([accuracy, cross_entropy], feed_dict={inputs: x_test_batch,
                                                                       labels: y_test_batch})
            print("CROSS-VALIDATION ACCURACY and loss at %d: %.5f , %.5f " % (step, acc, loss))

        if step % config["iterations_per_checkpoints"] == 0:
            saver.save(sess, os.path.join(LOG_DIR_SAVE, "w2v_model.ckpt"), step)

        sess.run(train_step, feed_dict={inputs: x_batch,
                                        labels: y_batch}
                 )


def loadAndTest(session):
    saver = tf.train.Saver()
    # Checkpoint
    ckpt = tf.train.get_checkpoint_state(LOG_DIR_LOAD)
    print('Loading model: ', ckpt.model_checkpoint_path)
    # Restore the model at the checkpoint
    saver.restore(session, ckpt.model_checkpoint_path)
    test(session)



with open('LSTM_Model_config.json') as config_file:
    config = json.load(config_file)

np.random.seed(config["random_seed"])
LOG_DIR_SAVE = config["save_model_to_directory"]
LOG_DIR_LOAD = config["load_model_from_directory"]
train_path = config["train_npy_file_path"]
test_path = config["test_npy_file_path"]
shuffle_ = config["shuffle"]

# Hyperparameters:
batch_size = config["batch_size"]
hidden_layer_size = config["hidden_layer_size"]
num_LSTM_layers = config["num_of_lstm_layers"]

# Data spesific:
num_of_classes = config["num_of_classes"]
time_steps = config["num_of_time_steps"]
num_of_features = config["num_of_features"]

dp = DataPipe(train_path, test_path)

inputs = tf.placeholder(dtype=tf.float32, shape=[batch_size, time_steps, num_of_features])
labels = tf.placeholder(dtype=tf.float32, shape=[batch_size, num_of_classes])

with tf.variable_scope("lstm"):
    def lstm_cell():
        lstm = tf.nn.rnn_cell.BasicLSTMCell(hidden_layer_size, forget_bias=1.0, )
        return lstm


    # cells = tf.contrib.rnn.MultiRNNCell(cells= [lstm_cell() for _ in range(num_LSTM_layers)] if num_LSTM_layers != 1 else lstm_cell(),
    #                                   state_is_tuple=True)
    outputs, states = tf.nn.dynamic_rnn(lstm_cell(), inputs,
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
final_output = tf.matmul(states[1], weights["linear_layer"]) + biases["linear_layer"]
softmax = tf.nn.softmax_cross_entropy_with_logits(logits=final_output,
                                                  labels=labels)
cross_entropy = tf.reduce_mean(softmax)
train_step = tf.train.RMSPropOptimizer(0.001, 0.9).minimize(cross_entropy)
correct_prediction = tf.equal(tf.argmax(labels, 1),
                              tf.argmax(final_output, 1))
accuracy = (tf.reduce_mean(tf.cast(correct_prediction,
                                   tf.float32))) * 100

with tf.Session() as sess:
    if config["mode"] == "train":
        train(sess)
    elif config["mode"] == "test":
        loadAndTest(sess)
    else:
        print("config[mode] : train or test ")

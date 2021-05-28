import numpy as np
import matplotlib.pyplot as plt


def soft_max(x):
    return np.exp(x) / np.sum(np.exp(x), axis=0)

def arg_max(sample_rewards,samples):
    times = 4
    list_samp = []
    list_rew = []
    for i in range(times):
        arg = np.argmax(sample_rewards)
        list_samp.append(samples[arg])
        list_rew.append(sample_rewards[arg])
        sample_rewards[arg] = -1
    return list_samp, list_rew

p1 = np.random.uniform(low=0.1, high = 0.5, size = 1)
p2 = np.random.uniform(low=0.1, high = 0.5, size = 1)
p3 = np.random.uniform(low=0.1, high = 0.25, size = 1)
p4 = np.random.uniform(low=0.1, high = 0.25, size = 1)
p5 = np.random.uniform(low=0.5, high = 1.5, size = 1)

def unknown_reward_function(x, noise = 0):
    r = (p5*np.exp(-np.power(x+p1,2.)/(2*np.power(p3,2.)))
    +np.exp(-np.power(x-p2,2.)/(2*np.power(p4,2.))))
    return r + (noise*(np.random.random()-0.5)/10.)

x = np.linspace(-1,1,200)
y = unknown_reward_function(x,0)
plt.xlabel('X', size = 15)
plt.ylabel('R', size = 15)
plt.plot(x,y,color = 'red')
y_max = max(y)
print(y_max)
#plt.show()
mu = 0
sigma = 1
sample_size = 10

global_max_samples = []
global_max_rewards = []

step = 0
while sigma>1.0e-3:
    samples = []
    while len(samples) < sample_size:
        sample = (np.random.normal(mu, sigma, 1))[0]
        if sample<=1 and -1<=sample:
            samples.append(sample)

    sample_rewards = []
    for sample in samples:
        r = (unknown_reward_function(sample,0))[0]
        sample_rewards.append(r)

    x = np.linspace(-1, 1, 200)
    y = unknown_reward_function(x, 0)
    plt.xlabel('X', size=15)
    plt.ylabel('R', size=15)
    plt.plot(x, y, color='red')
    plt.scatter(samples, sample_rewards)
    plt.draw()
    plt.pause(0.2)
    plt.clf()

    """""
    sample_deviations = []
    for sample in samples:
        sample_deviations.append()
    """

    max_samples, max_rewards = arg_max(sample_rewards.copy(),samples.copy())
    global_max_rewards = global_max_rewards + max_rewards
    global_max_samples = global_max_samples + max_samples

    helper = global_max_rewards.copy()
    helper[0] = helper[0] + step * 0.01
    helper[1] = helper[1] + step * 0.01
    sample_weights = soft_max(helper)
    new_mean = sum([a*b for a, b in zip(sample_weights,global_max_samples)])
    mu = new_mean

    if sigma>0.3:
        sigma = sigma-0.2
    elif sigma>0.2:
        sigma = sigma-0.02
    else:
        sigma = sigma/4

    print("global_maximum_samples are:\t" + str(global_max_samples))
    print("global_maximum_weights are:\t" + str(sample_weights))
    print("global_maximum_rewards are:\t" + str(global_max_rewards))
    print("new mu is:" + str(mu) + " new sigma is:" + str(sigma))
    print()

    step = step + 1

    max = 0
    second_max = 0
    for i in global_max_rewards:
        if i >second_max and i<= max:
            second_max = i
        elif i>second_max and i>max:
            max = i


    index_max = global_max_rewards.index(max)
    index_sec_max = global_max_rewards.index(second_max)
    max_sample = global_max_samples[index_max]
    second_max_sample = global_max_samples[index_sec_max]

    global_max_samples.clear()
    global_max_samples.append(max_sample)
    global_max_samples.append(second_max_sample)
    global_max_rewards.clear()
    global_max_rewards.append(max)
    global_max_rewards.append(second_max)










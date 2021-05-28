# -*- coding: utf-8 -*-
import collections
import json
import re

punct = re.compile(r'(\w+)')


PATH_FOR_TXT_FILE = "base.txt"
PATH_FOR_1_GRAM_JSON = '1-grams_count_dictionary.json'
PATH_FOR_2_GRAM_JSON = '2-grams_count_dictionary.json'

#str(input("1-gram json file name withOUT .json")) + ".json"# to take the file names from user.


"""
def three_grams():
    c3grams = collections.defaultdict()
    c3grams_list = []
    de3grams = collections.deque()
    wordsdeq3 = collections.deque()

    f = open(PATH_FOR_TXT_FILE,"r")
    for line in f:
        tokenized = [m.group() for m in punct.finditer(line)]
        line = ' '.join(tokenized)

        line = line.strip("\n").lower()
        theline = line.split(" ")

        new_line = []
        for e in theline:
            if not e :
                continue
            else:
                new_line.append(e)
        theline = new_line
        del new_line

        if len(theline) < 3:
            continue

        for word in theline:
            if word:
                wordsdeq3.append(word)
        #wordsdeq3.appendleft("<S>")
        #wordsdeq3.append("<E>")

        for i in range(2):
            de3grams.append(wordsdeq3.popleft())

        while wordsdeq3:
            newWord = wordsdeq3.popleft()

            de3grams.append(newWord)

            g3 = " ".join(de3grams)

            #c3grams[g3] = c3grams.get(g3, 0) + 1
            c3grams_list.append(g3)

            de3grams.popleft()

        de3grams.clear()

    f.close()
    del wordsdeq3
    del de3grams
    
    f3 = open("3-grams_count_dictionary.json","x")
    c3grams = dict(sorted(c3grams.items(), key=lambda x: x[1], reverse= True))
    f3.write(json.dumps(c3grams))

    f3 = open("3-grams.txt","x")

    for line in c3grams_list:
        f3.write(line + "\n")
"""
def two_grams():
    c2grams = collections.defaultdict()
    c2grams_list = []
    de2grams = collections.deque()
    wordsdeq2 = collections.deque()

    f = open(PATH_FOR_TXT_FILE,"r")
    for line in f:
        tokenized = [m.group() for m in punct.finditer(line)]
        line = ' '.join(tokenized)

        line = line.strip("\n").lower()
        theline = line.split(" ")

        new_line = []
        for e in theline:
            if not e:
                continue
            else:
                new_line.append(e)
        theline = new_line
        del new_line

        if len(theline) < 2:
            continue

        for word in theline:
            if word:
                wordsdeq2.append(word)
        wordsdeq2.appendleft("<S>")
        wordsdeq2.append("<E>")

        for i in range(1):
            de2grams.append(wordsdeq2.popleft())

        while wordsdeq2:
            newWord = wordsdeq2.popleft()

            de2grams.append(newWord)

            g2 = " ".join(de2grams)

            c2grams[g2] = c2grams.get(g2, 0) + 1
            c2grams_list.append(g2)

            de2grams.popleft()

        de2grams.clear()

    f.close()

    del wordsdeq2
    del de2grams

    f2 = open(PATH_FOR_2_GRAM_JSON, "w+")
    c2grams = dict(sorted(c2grams.items(), key=lambda x: x[1], reverse=True))
    f2.write(json.dumps(c2grams))


"""
f2 = open("2-grams.txt","+w")

for line in c2grams_list:
    f2.write(line + "\n")
"""

def one_grams():
    c1grams = collections.defaultdict()
    c1grams_list = []
    de1grams = collections.deque()
    wordsdeq1 = collections.deque()

    f = open(PATH_FOR_TXT_FILE,"r")
    for line in f:
        tokenized = [m.group() for m in punct.finditer(line)]
        line = ' '.join(tokenized)

        line = line.strip("\n").lower()
        theline = line.split(" ")

        new_line = []
        for e in theline:
            if not e:
                continue
            else:
                new_line.append(e)
        theline = new_line
        del new_line

        if len(theline) < 1:
            continue

        for word in theline:
            if word:
                wordsdeq1.append(word)
        wordsdeq1.appendleft("<S>")
        wordsdeq1.append("<E>")

        for i in range(0):
            de1grams.append(wordsdeq1.popleft())

        while wordsdeq1:
            newWord = wordsdeq1.popleft()

            de1grams.append(newWord)

            g1 = de1grams[0]

            c1grams[g1] = c1grams.get(g1, 0) + 1
            c1grams_list.append(g1)

            de1grams.popleft()

        de1grams.clear()

    f.close()

    del wordsdeq1
    del de1grams
    f1 = open(PATH_FOR_1_GRAM_JSON, "w+")
    c1grams = dict(sorted(c1grams.items(), key=lambda x: x[1], reverse=True))
    f1.write(json.dumps(c1grams))
"""
f1 = open("1-grams.txt","+w")
for line in c1grams_list:
    f1.write(line + "\n")
"""



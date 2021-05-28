# encoding:utf-8
import math
import warnings
import json
import numpy as np
import formatter
#import gensim
#from gensim.test.utils import get_tmpfile
#from gensim.models import KeyedVectors
warnings.filterwarnings(action='ignore')

#turkish_valid_words txt file path
#VALID_WORDS_PATH = "valid_words.txt" # for valid words in validSet()
LEARN_FROM_PATH = "base.txt" # was for calculating frequencies for Zipf's law createDicFromTxt
TEXT_FILE = "text.txt"

print("default file name for base text file is:\t\t\tbase.txt\ndefault file name "
      "default file name for text file to be corrected  is:\t\ttext.txt\n"
      " ")

"""
paths = inp
paths = paths.strip()
if paths != "":
    VALID_WORDS_PATH = paths[0].strip()
    LEARN_FROM_PATH = paths[1].strip()
    TEXT_FILE = paths[2].strip()
"""


def word2vec():
    f = open("cbotv5.txt.txt", "r")
    valids = open("turkish_valid_words.txt", "r")
    validset = set()
    a = valids.readline()

    while a != "":
        for word in a.split(" "):
            if word:
                word = (word.strip('\n')).lower()
                if len(word) != 1:
                    validset.add(word)
        a = valids.readline()

    data = []
    c = f.readline()

    while c != "":
        sentence = []
        for word in c.split(" "):
            if word:
                currword = (word.strip('\n')).lower()
                sentence.append(currword)
        data.append(sentence)
        c = f.readline()

    # Create CBOW model
    model =1
   #delete the above line, uncomment this line, and install gensim package = gensim.models.Word2Vec(data, min_count=1, size=3000, window=10)

    #word_vectors = model.wv
    #word_vectors.save_word2vec_format("vectors4000window10")
    #word_vectors = KeyedVectors.load(fname, mmap='r')


    return model, validset

    # Print results

    # print("Cosine similarity between 'araba' " +
    #      "and 'fi' - CBOW : ",
    #      model.similarity('araba', 'fi'))

    # print("Cosine similarity between 'müteri' " +
    #      "and 'hizmetleri' - CBOW : ",
    #      model.similarity('müteri', 'hizmetleri'))

def validSet():
    #valids = open(VALID_WORDS_PATH, "r")
    valids = open(LEARN_FROM_PATH, "r")
    validset = set()
    a = valids.readline()

    while a != "":
        for word in a.split(" "):
            if word:
                word = (word.strip()).lower()
                validset.add(word)
                """
                if len(word) != 1:
                    validset.add(word)
                """
        a = valids.readline()

    return validset
# returns a dictionary where the words in the given .txt file are keys, and their number of occurrences are the
# values. dictionary is sorted in descending order wrt the values.
def createDicFromTxt():
    wordsDic = {}
    defpath = LEARN_FROM_PATH
    f = open(defpath, "r")
    # fills the dictionary with the words in the file
    c = " "
    while c != "":
        for word in c.split(" "):
            #currword = "<S> "+ ((word.strip('\n')).lower()) + " <E>"
            currword = ((word.strip('\n')).lower())
            if (len(currword) == 1 and currword != "o") or (currword == ''):
                continue
            if currword in wordsDic:
                wordsDic[currword] = wordsDic[currword] + 1
            else:
                wordsDic[currword] = 1
        c = f.readline()
    return (dict(sorted(wordsDic.items(), key=lambda x: x[1], reverse=True)))



def loadGrams():
    json_file = open('2-grams_count_dictionary.json', "r")
    data2 = json.load(json_file)
    json_file = open('1-grams_count_dictionary.json', "r")
    data1 = json.load(json_file)

    return data1, data2

# given dictionary of words and number of occurences, calculates the "points" for each word, and changes the values
# with these points. (Zipf's Law is used for the words' frequencies)
def createDataBaseFromDic(wordsDic):
    length = len(wordsDic)
    factor = math.log(length)
    i = 1
    for k, v in wordsDic.items():
        prob = (1 / (i * factor))
        wordsDic[k] = prob
        i = i + 1


def softMax(database):
    sumOfExp = 0
    for k, v in database.items():
        sumOfExp = sumOfExp + np.exp(v)

    """Compute softmax values for each sets of scores in x."""
    for k, v in database.items():
        database[k] = np.exp(v) / sumOfExp

    # creates "cost" and "separate" tables for the substrings of the entry, in a bottom up manner.


def bottomUpTable(entry, database):
    n = len(entry)
    # includesword = [[False for x in range(n + 1)] for y in range(n + 1)]
    separate = [[0 for x in range(n + 1)] for y in range(n + 1)]
    point = [[0 for x in range(n + 1)] for y in range(n + 1)]
    # all length of 1 substrings' cost points are got from the database
    for x in range(n):
        point[x][x + 1] = database.get(str(entry[x:x + 1]), 0)
    # all length of more than 1 substrings' points are evaluated from the smaller sub strings and directly from
    # the database.
    for L in range(2, n + 1):
        for i in range(n - L + 1):
            j = i + L
            point[i][j] = 0
            for k in range(i + 1, j):
                q = point[i][k] * point[k][j]
                if q > point[i][j]:
                    point[i][j] = q
                    separate[i][j] = k

            q = database.get(str(entry[i:j]), 0)
            if q * q * 100 > point[i][j]:
                point[i][j] = q
                separate[i][j] = 0
    return separate


# recursively separates the given entry, according to the separate table,
def separateIt(entry, separate, start, end):
    cut = separate[start][end]
    if cut == 0:
        return entry
    entry1 = str(entry[:cut - start])
    entry2 = str(entry[cut - start:])
    return separateIt(entry1, separate, start, cut) + " " + separateIt(entry2, separate, cut, end)


def edit(word, database, validset, entryWords, valids, index, data1, data2):
    if word in validset:
        return word

    # f = open("cpossibles.txt", "x")

    letters = "abcçdefgğhıijklmnoöprsştuüvyz "

    splits1 = [(word[:i], word[i:]) for i in range(len(word) + 1)]

    deletes1 = [A + B[1:] for A, B in splits1 if B]
    inserts1 = [A + c + B for A, B in splits1 for c in letters]
    replaces1 = [A + c + B[1:] for A, B in splits1 for c in letters]
    transposes1 = [A + B[1] + B[0] + B[2:] for A, B in splits1 if len(B) > 1]

    possibleEdits1 = set(deletes1 + replaces1 + inserts1 + transposes1)
    # f.write("1dist: \n")
    # f.write(str(possibleEdits1) + "\n")

    #deletespool = []
    #insertspool = []
    #replacespool = []
    #for pe in possibleEdits1:
    #    splits2 = [(pe[:i], pe[i:]) for i in range(len(pe) + 1)]

    #    deletespool = deletespool + [A + B[1:] for A, B in splits2 if B]
    #    replacespool = replacespool + [A + c + B[1:] for A, B in splits2 for c in letters]
    #    insertspool = insertspool + [A + c + B for A, B in splits2 for c in letters]
    #    transposespool = [A + B[1] + B[0] + B[2:] for A, B in splits2 if len(B) > 1]

    #possibleEdits2 = set(deletespool + replacespool + insertspool + transposespool)
    # f.write("2distinserts: \n")
    # f.write(str(possibleEdits2) + "\n")

    #for e in possibleEdits2.copy():
    #   if e not in validset:
    #        possibleEdits2.remove(e)

    for e in possibleEdits1.copy():
        if e not in validset:
            possibleEdits1.remove(e)

    # f.write("1dist after remove: \n")
    # f.write(str(possibleEdits1) + "\n")
    # f.write("2dist after remove : \n")
    # f.write(str(possibleEdits2)+ "\n")

    edited = ""
    maximum = 0

    for e in possibleEdits1:
        if e in database:
            current = calculateProbability(e, index, entryWords, valids, data1, data2)*0.8
            if current > maximum:
                maximum = current
                edited = e

    #for e in possibleEdits2:
    #    if e in database:

    #        current = calculateProbability(e,index, entryWords, valids, data1, data2)*0.2
    #        if current > maximum:
    #            maximum = current
    #            edited = e

    #print(database.get(edited, 0))
    if "" == edited:
        for e in possibleEdits1:
            if e in validset:
                return e

        #for e in possibleEdits2:
        #    if e in validset:
        #        return e

        return word
    valids[index] = True
    return edited


def calculateProbability(w, index, entryWords, valids, unigrams, bigrams):

    #print ("correction of: " + str(entryWords[index] + " is:  " + w))
    low = index - 1
    high = index + 1

    down = 0
    upper = 0

    uni = unigrams.get(w, 0) / sum(unigrams.values())
    uni = uni * uni
    if valids[low]:
        #print(entryWords[low])
        down = bigrams.get(str(entryWords[low]) + " " + w, 0) / unigrams.get(entryWords[low], 0)
        if valids[high]:
            upper = bigrams.get(w + " " + str(entryWords[high]), 0) / unigrams.get(w, 0)
            bi = down * upper
            #print(" upper is: "  + str(upper) + " down is :" + str(down)+ " bi is: " + str(bi) + " uni is: " + str(uni) + " , total is " + str(uni * 0.25 + bi * 0.75))
            return uni * 0.25 + bi * 0.75
        else:
            bi = down * down
            #print(" upper is: none," + " down is :" + str(down) + " bi is: " + str(bi) + " uni is: " + str(uni) + " , total is " + str(uni * 0.25 + bi * 0.75))
            return uni * 0.25 + bi * 0.75
    else:
        if valids[high]:
            upper = bigrams.get(w + " " + str(entryWords[high]), 0) / unigrams.get(w, 0)
            bi = upper * upper
            #print(" upper is: " + str(upper) + " down is none: " + " bi is: " + str(bi) + " uni is: " + str(uni) + " , total is " + str(uni * 0.25 + bi * 0.75))
            return uni * 0.25 + bi * 0.75
        else:
            #print(" upper is: none, " + str(upper) + " down is: none, " + " bi is: "+ " uni is: " + str(uni) + " , total is " + str(uni))
            return uni

def main():
    database = createDicFromTxt()
    createDataBaseFromDic(database)
    validset = validSet()

    try:
        f = open('2-grams_count_dictionary.json')
        f.close()
    except IOError:
        formatter.two_grams()
    try:
        f = open('1-grams_count_dictionary.json')
        f.close()
    except IOError:
        formatter.one_grams()

    data1, data2 = loadGrams()
    #model, --validset = word2vec()
    # f = open("data.txt", "x")
    # for k, v in database.items():
    #   f.write(str(k) + "\t" + str(v) + "\n")
    f = open(TEXT_FILE, "r")
    f2 = open("ekin_corrected.txt", "w+")
    for entry in f:
        #entry = input("Entry Girebilirsiniz: ")
        entry = (entry.strip("\n")).lower()
        entry = entry.split(" ")
        separatedentries = []
        for ent in entry:
            separatedentries.append(ent)
        """
        for ent in entry:
            length = len(ent)
            if '' == ent:
                continue
            separate = bottomUpTable(ent, database)
            separatedentries.append(str(separateIt(ent, separate, 0, length)))
        """
        output = ""
        entryWords = []
        entryWords.append("<S>")
        for se in separatedentries:
            words = se.split(" ")
            entryWords = entryWords + words
        entryWords.append("<E>")

        valids = [False] * len(entryWords)
        count = 0
        for ew in entryWords.copy():
            if ew in database:
                valids[count] = True
            count = count + 1

        count = 0
        for ew in entryWords.copy():
            if ((ew != "<S>") and (ew != "<E>")) and ew not in validset:
                entryWords[count] = edit(ew, database, validset, entryWords, valids, count, data1, data2)
            count = count + 1

        f2.write((" ".join(entryWords).lstrip("<S> ").rstrip(" <E>")) +  "\n")


if __name__ == '__main__':
    main()

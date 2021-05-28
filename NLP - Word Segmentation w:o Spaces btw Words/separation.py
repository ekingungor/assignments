import math


# returns a dictionary where the words in the given .txt file are keys, and their number of occurrences are the
# values. dictionary is sorted in descending order wrt the values.
def createDicFromTxt():
    wordsDic = {}
    defpath = "base.txt"
    f = open(defpath, "r")
    print("\nDefault Dataset'i Kullandınız.")

    # fills the dictionary with the words in the file
    c = " "
    while c != "":
        for word in c.split(" "):
            currword = (word.strip('\n')).lower()
            if currword in wordsDic:
                wordsDic[currword] = wordsDic[currword] + 1
            else:
                wordsDic[currword] = 1
        c = f.readline()
    return (dict(sorted(wordsDic.items(), key=lambda x: x[1], reverse=True)))


# given dictionary of words and number of occurences, calculates the "points" for each word, and changes the values
# with these points. (Zipf's Law is used for the words' frequencies)
def createDataBaseFromDic(wordsDic):
    length = len(wordsDic)
    factor = math.log(length)
    i = 1
    for k, v in wordsDic.items():
        prob = (1 / (i * factor))
        i = i + 1
        wordsDic[k] = prob


# creates "cost" and "separate" tables for the substrings of the entry, in a bottom up manner.
def bottomUpTable(entry, database):
    n = len(entry)
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
            if q > point[i][j]:
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


def main():
    database = createDicFromTxt()
    createDataBaseFromDic(database)
    f2 = open("corrected.txt","w+")
    f1 = open("text.txt","r")
    for entry in f1:
        entry = entry.strip().lower()
        old_words = entry.split(" ")
        new_words = []
        for word in old_words:
           # word = entry.replace(' ', '')
            length = len(word)
            separate = bottomUpTable(word, database)
            n_word = str(separateIt(word, separate, 0, length))
            new_words.append(n_word)
        line = " ".join(new_words)
        f2.write(line + "\n")

if __name__ == '__main__':
    main()

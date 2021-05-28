import argparse
import operator
import os
import hashlib

from _collections import defaultdict
from Thing import Thing
from pathlib import Path

parser = argparse.ArgumentParser()
fdgroup = parser.add_mutually_exclusive_group()
# -f and -d options are mutually exclusive
fdgroup.add_argument('-f', action='store_true')
fdgroup.add_argument('-d', action='store_true')
parser.add_argument('-c', action='store_true')
parser.add_argument('-n', action='store_true')
parser.add_argument('-s', action='store_true')
parser.add_argument("paths", nargs='*', type=str, default=".")
args = parser.parse_args()

newPath = []
# if there is any realtive path, they are converted to absolute path and all are included in newPath list
for path in args.paths:
    newPath.append(os.path.abspath(path))
args.paths = newPath

# given requirements are if f and d is not given, then it's f
if not args.f and not args.d:
    args.f = True
# if not -n and not -c then it's c
if not args.n and not args.c:
    args.c = True
# if it's only -n then -s is ignored
if args.n and not args.c and args.s:
    args.s = False
# hash of empty string is in emptyHash
m = hashlib.sha256()
m.update(b"")
emptyHash = m.hexdigest()
# hash of the has of empty string is in hashOfEmptyHash
s = hashlib.sha256()
s.update(emptyHash.encode('utf-8'))
hashOfEmptyHash = s.hexdigest()

# dictionaries to be used will accumulate here
supremeDictionary = {}
# to print we use the reverse of supreme dictionary
reversedDictionary = defaultdict(list)


# if it's -d, this method will work and fill the supremeDictionary, keys will be the path of the dictionary and
# values will be the corresponding Thing object.
def directoriesWork(root, dirs, files):
    # total size of this directory (root)
    counter = 0
    # used to temporarily hold this directoriy's contents
    Dic = {}
    # if empty directory, then contentHash is hashOfEmptyHash, nameHash is the hash of the concatenation of
    # emptyHash and directory name.
    if not files and not dirs:
        dirname = getTheDirectoryNameFromPath(root)
        contenthash = emptyHash
        concat = dirname
        namehash = hashThisString(concat)
        d = Thing(dirname, contenthash, namehash, 0)
        supremeDictionary[root] = d

    # otherwise for each file contenthash namehash size and filename used to create a Thing object and this object stored in
    # Dic
    else:
        for filename in files:
            filepath = os.path.join(root, filename)
            contenthash = hashIt(filepath)
            namehash = hashThisString(filename)
            size = Path(filepath).stat().st_size
            counter = counter + size
            f = Thing(filename, contenthash, namehash, size)
            Dic[filepath] = f

        for directoryname in dirs:
            directorypath = os.path.join(root, directoryname)
            d = supremeDictionary[directorypath]
            counter = counter + d.getSize()
            Dic[directorypath] = d

        dirname = getTheDirectoryNameFromPath(root)
        a = list(Dic.values())
        # after all contents of directory are in Dic, the contents' contenthashes are in objectslistforcontent, and
        # namehashes are in objectslistforname, sorted.
        objectslistforcontent = []
        objectslistforname = []
        for item in a:
            objectslistforcontent.append(item.getContentHash())
        for item in a:
            objectslistforname.append(item.getNameHash())
        objectslistforcontent = sorted(objectslistforcontent)
        objectslistforname = sorted(objectslistforname)
        # for the namehash of this directory all sorted namehashes are appended to this directories name and hashed
        # for the contenthash of this directory all sorted contenthashes are concatenated and hashed
        newcontenthash = ""
        newnamehash = dirname
        for o in objectslistforcontent:
            newcontenthash = newcontenthash + o
        for b in objectslistforname:
            newnamehash = newnamehash + b

        newcontenthash = hashThisString(newcontenthash)
        newnamehash = hashThisString(newnamehash)

        # a Thing object is created with calculated hashes and size, and the object is inserted to the dictionary with the key being its path
        d = Thing(dirname, newcontenthash, newnamehash, counter)
        supremeDictionary[root] = d
        Dic.clear()


# if it's -f, this method will work and fill the supremeDictionary, keys will be the path of the file and
# values will be the corresponding Thing object.
def filesWork(root, files):
    # for all files given in this directory with path root, name and content hashes of files are calculated and the corresponding Thing
    # object is inserted in the supremeDictionary
    for filename in files:
        filepath = os.path.join(root, filename)
        contenthash = hashIt(filepath)
        namehash = hashThisString(filename)
        size = Path(filepath).stat().st_size
        f = Thing(filename, contenthash, namehash, size)
        supremeDictionary[filepath] = f


# given a full path, returns the name of the directory
def getTheDirectoryNameFromPath(fullpath):
    if "/" in fullpath:
        index = fullpath.rfind("/")
    elif "\\" in fullpath:
        index = fullpath.rfind("\\")
    X = fullpath[index + 1:]
    return X


# given a filepath, returns the hash of its contents
def hashIt(filePath):
    file = filePath
    BLOCK_SIZE = 65536
    file_hash = hashlib.sha256()
    with open(file, 'rb') as f:
        fb = f.read(BLOCK_SIZE)
        while len(fb) > 0:
            file_hash.update(fb)
            fb = f.read(BLOCK_SIZE)
    return file_hash.hexdigest()


# given a string, returns the hash value of this string
def hashThisString(name):
    h = hashlib.sha256()
    h.update(name.encode('utf-8'))
    return h.hexdigest()


# executing the program
# if -d option is true, then all directories in given paths are given to the directoriesWork method, bottom up manner
# else if -f option is given then all files in given paths are given to the filesWork method, topdown manner
for path in args.paths:
    if args.d:
        for root, dirs, files in os.walk(path, topdown=False):
            directoriesWork(root, dirs, files)
    if args.f:
        for root, dirs, files in os.walk(path):
            filesWork(root, files)

# if it's -c, then a new dictionary is formed by reversing the supremeDictionary, where the keys are hashValues and the values are paths
# hasValues can be either contentHash or nameHash according to the given optin -c or -n or -cn
# and also size values are added to the dictionary's values, forming a (path,size) tuple
if args.c and not args.n:
    for key in supremeDictionary:
        value = (supremeDictionary[key]).getContentHash()
        if args.s:
            size = (supremeDictionary[key]).getSize()
            reversedDictionary[value].append((key, size))
        else:
            reversedDictionary[value].append(key)
elif args.n and not args.c:
    for key in supremeDictionary:
        value = (supremeDictionary[key]).getNameHash()
        if args.s:
            size = (supremeDictionary[key]).getSize()
            reversedDictionary[value].append((key, size))
        else:
            reversedDictionary[value].append(key)
else:
    for key in supremeDictionary:
        contentvalue = (supremeDictionary[key]).getContentHash()
        namevalue = (supremeDictionary[key]).getNameHash()
        if args.s:
            size = (supremeDictionary[key]).getSize()
            (reversedDictionary[(contentvalue, namevalue)]).append((key, size))
        else:
            (reversedDictionary[(contentvalue, namevalue)]).append(key)



# after forming the reversedDictionary, now time to print identical ones. if -s option is true then , for each key in
# reverseDictionary if some key's value has more than one element, then the paths of these values and the sizes will
# be printed. the order for this is, first the identical paths are sorted wrt their sizes, after they are sorted wrt
# their paths' alphabetical order.
if args.s:
    helper = []
    for key in reversedDictionary:
        if len(reversedDictionary[key]) > 1:
            size = ((reversedDictionary[key])[0])[1]
            sortedbyname = sorted(reversedDictionary[key], key=lambda tup: tup[0])
            helper2 = []
            for element in sortedbyname:
                helper2.append(element[0])
            helper.append((helper2, size))

    sortedbynamefirst = sorted(helper, key=lambda tup: tup[0])
    sortedbysize = sorted(sortedbynamefirst, key=lambda tup: tup[1], reverse=True)

    for group in sortedbysize:
        size = group[1]
        for element in group[0]:
            print(element + "\t" + str(size))
        print()

# if -s option is false then , for each key in reverseDictionary if some key's value has more than one element,
# then the paths of these values will be printed. the order for this is, the paths' alphabetical order.
else:
    helper = []
    for key in reversedDictionary:
        if len(reversedDictionary[key]) > 1:
            somelist = sorted(reversedDictionary[key])
            helper2 = []
            helper.append(somelist)

    sortedbynamefirst = sorted(helper)
    for group in sortedbynamefirst:
        for element in group:
            print(element)
        print()


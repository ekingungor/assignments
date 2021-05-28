class Thing:

    def __init__(self, name, contenthash, namehash, size):
        self.contenthash = contenthash
        self.namehash = namehash
        self.name = name
        self.size = size

    def getContentHash(self):
        return self.contenthash

    def getNameHash(self):
        return self.namehash

    def getName(self):
        return self.name

    def getSize(self):
        return self.size



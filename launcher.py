import os
import sys


def main():
    fileName    = sys.argv[1]
    kMax        = int(sys.argv[2])
    anons       = ["dp", "greedy"]
    builds      = ["constr", "prio"]

    for k in range(3, kMax + 1):
        for anon in anons:
            for build in builds:
                command = "java -jar Anonymizer.jar %s %s %s %s" % (fileName, k, anon, build)
                # print(command)
                os.system(command)   
 

if __name__ == "__main__":
    main()

import os
import sys

fileName = sys.argv[1]
kMax = int(sys.argv[2]) + 1
anons = ["dp", "greedy"]
builds = ["constr", "prio"]
javaFile = "Anonymizer.jar"
command = "java -jar %s %s %s %s %s"

print()
print("  Python JAR launcher --> %s\n" % javaFile)
print("  [ * ] Loading files")
print("  [ * ] Anonymizing with multiple values of k: [3 - %s]" % (kMax-1))

i = 0
for k in range(3, kMax):
    for anon in anons:
        for build in builds:
            os.system(command % (javaFile, fileName, k, anon, build))
            percentage = int(100*i/(len(builds)*len(anons)*(kMax-3)))
            print("  [ * ] Percentage: %s%%" % percentage, end="\r")
            i += 1
print("  [ * ] Percentage: 100%")
print("  [ * ] Process completed!")

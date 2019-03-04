import pandas as pd
import matplotlib.pyplot as plt
import numpy
import sys

filename = sys.argv[1]
fieldNames = ["file_name", "k", "algo_anon", "algo_constr", "cost", "time", "feasible"]
dtypes = {"file_name": str, "k": int, "algo_anon": str, "algo_constr": str, "cost": int, "time": int, "feasible": bool}

# Leggo i dati
data = pd.read_csv(filename, delimiter=";", sep=";", header=None, names=fieldNames, dtype=dtypes)

# Carico i dati relativi a costo e tempo selezionando per tipo di algoritmo ed escludendo i caso con tempo nullo
X = {"dp": {"constr": [], "prio": []}, "greedy": {"constr": [], "prio": []}}

'''
X["dp"]["constr"]   = data.loc[(data["algo_anon"] == "dp") & (data["algo_constr"] == "constr") & (data["time"] != 0), ["k", "cost", "time"]].values
X["dp"]["prio"]     = data.loc[(data["algo_anon"] == "dp") & (data["algo_constr"] == "prio") & (data["time"] != 0), ["k", "cost", "time"]].values

X["greedy"]["constr"]   = data.loc[(data["algo_anon"] == "greedy") & (data["algo_constr"] == "constr") & (data["time"] != 0), ["k", "cost", "time"]].values
X["greedy"]["prio"]     = data.loc[(data["algo_anon"] == "greedy") & (data["algo_constr"] == "prio") & (data["time"] != 0), ["k", "cost", "time"]].values
'''

X["dp"]["constr"] = data.loc[(data["algo_anon"] == "dp") & (data["algo_constr"] == "constr") & (data["feasible"] == True), ["k", "cost", "time"]].values
X["dp"]["prio"] = data.loc[(data["algo_anon"] == "dp") & (data["algo_constr"] == "prio") & (data["feasible"] == True), ["k", "cost", "time"]].values

X["greedy"]["constr"] = data.loc[(data["algo_anon"] == "greedy") & (data["algo_constr"] == "constr") & (data["feasible"] == True), ["k", "cost", "time"]].values
X["greedy"]["prio"] = data.loc[(data["algo_anon"] == "greedy") & (data["algo_constr"] == "prio") & (data["feasible"] == True), ["k", "cost", "time"]].values

X1 = X["dp"]["constr"]
X2 = X["dp"]["prio"]
X3 = X["greedy"]["constr"]
X4 = X["greedy"]["prio"]

'''

# Plotto i dati
plt.figure(1)


plt.subplot(221)
plt.plot(X1[:,0], X1[:,1], '-o', X1[:,0], X1[:,2], '-o')
plt.xlabel("k values")
plt.legend(["Cost (edges)", "Time (ms)"])
plt.title("anon: DP constr: CONSTR")


plt.subplot(222)
plt.plot(X2[:,0], X2[:,1], '-o', X2[:,0], X2[:,2], '-o')
plt.xlabel("k values")
plt.legend(["Cost (edges)", "Time (ms)"])
plt.title("anon: DP constr: PRiORITY")

plt.subplot(223)
plt.plot(X3[:,0], X3[:,1], '-o', X3[:,0], X3[:,2], '-o')
plt.xlabel("k values")
plt.legend(["Cost (edges)", "Time (ms)"])
plt.title("anon: GREEDY constr: CONSTR")

plt.subplot(224)
plt.plot(X4[:,0], X4[:,1], '-o', X4[:,0], X4[:,2], '-o')
plt.xlabel("k values")
plt.legend(["Cost (edges)", "Time (ms)"])
plt.title("anon: GREEDY constr: PRIORITY")

'''

plt.figure(1)

plt.subplot(211)
plt.plot(X1[:,0], X1[:,1], '-o', X2[:,0], X2[:,1], '-o', X3[:,0], X3[:,1], '-o', X4[:,0], X4[:,1], '-o')
#plt.yticks(list(range(0,20)))
plt.xticks(list(range(3,max(data["k"].values)+1)))
plt.grid(color='black', linestyle='--', linewidth=0.5)
plt.xlabel("K values")
plt.ylabel("COSTS (EDGES)")
plt.legend(["DP CONSTR", "DP PRIO", "GREEDY CONSTR", "GREEDY PRIO"])

plt.subplot(212)
plt.plot(X1[:,0], X1[:,2], '-o', X2[:,0], X2[:,2], '-o', X3[:,0], X3[:,2], '-o', X4[:,0], X4[:,2], '-o', label="times")
plt.xticks(list(range(3,max(data["k"].values)+1)))
plt.grid(color='black', linestyle='--', linewidth=0.5)
plt.xlabel("K values")
plt.ylabel("TIMES (ms)")
plt.legend(["DP CONSTR", "DP PRIO", "GREEDY CONSTR", "GREEDY PRIO"])

plt.show()

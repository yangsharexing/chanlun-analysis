#!/usr/bin/python

import numpy as np
import statsmodels.api as sm
import pandas as pd
import matplotlib.pyplot as plt

'''
测试

'''

data = pd.read_excel('C://test_demo.xls',header=None,index_col=None)

import numpy as np
X=[ 0 ,0.0076 ]
Y=[ -0.7692,-0.5707 ]


z1 = np.polyfit(X, Y, 1) #一次多项式拟合，相当于线性拟合
p1 = np.poly1d(z1)
print(z1) #[ 1.          1.49333333]
print(p1) # 1 x + 1.493


print(X);
print(Y);


#!/usr/bin/python
import baostock as bs
import pandas as pd
from sqlalchemy import create_engine
from redis import StrictRedis
import json
from logging import _startTime


import numpy as np
import matplotlib.pyplot as plt

'''
单元拟合：

'''

data = pd.read_excel('C://test.xls',header=None,index_col=None)
# yinliRate    preKLine    afterKLine    preVolume    afterVolume    preDiff    afterDiff    preZSLineNum    afterZSLineNum
yinliRate = data[0]

preKLine=data[1]
afterKLine=data[2]
preVolume=data[3]
afterVolume=data[4]
preDiff=data[5]
afterDiff=data[6]
preZSLineNum=data[7]
afterZSLineNum=data[8]

z1 = np.polyfit(afterDiff, yinliRate, 3)  #一次多项式拟合，相当于线性拟合
fx = np.poly1d(z1)
print(z1)   #[ 1.          1.49333333]
print(fx)   # 1 x + 1.493


# x = afterDiff
# y = yinliRate
# Y = fx(x)
# plt.title('afterKLine->yinliRate')
# plt.scatter(afterKLine,yinliRate,color='r',s=1,label='point')#x，y散点图
# plt.show()







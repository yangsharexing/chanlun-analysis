#!/usr/bin/python

import numpy as np
import statsmodels.api as sm
import pandas as pd
import matplotlib.pyplot as plt
from statsmodels.sandbox.distributions.examples.ex_transf2 import xx

'''
多元线性拟合：

'''

data = pd.read_excel('g://101test.xls',header=None,index_col=None)

# yinliRate    preKLine    afterKLine    preVolume    afterVolume    preDiff    afterDiff    preZSLineNum    afterZSLineNum    preMacd    afterMacd


# 由于成交量每个股票的区别太大，所以用前成交/后成交还表示

yinliRate = data[0]

preKLine=data[1]
afterKLine=data[2]
volume=data[3]/data[4]
preDiff=data[5]
afterDiff=data[6]
preZSLineNum=data[7]
afterZSLineNum=data[8]
preMacd=data[9]
afterMacd=data[10]


y=yinliRate
x1=preKLine
x2=afterKLine
x3=volume
x4=preDiff
x5=afterDiff
x6=preZSLineNum
x7=afterZSLineNum
x8=preMacd
x9=afterMacd
# x1,x2,x3,x4,x5,x6,x7,

x=np.column_stack((x8,x9))
 
# 线性回归拟合
x_n = sm.add_constant(x) #statsmodels进行回归时，一定要添加此常数项
model = sm.OLS(y, x_n) #model是回归分析模型
results = model.fit() #results是回归分析后的结果
 
#输出回归分析的结果
print(results.summary())
print('Parameters: ', results.params)
print('R2: ', results.rsquared)

# i = 0
# tip = 0;
# tipt = 0;
# while i <len(y):
#     v1 = -0.003672+0.000382*x3[i]+0.095904*x4[i]+0.649656*x5[i]+0.000263*x6[i]+0.000835*x7[i];
#     if(v1>0.1):
#         tipt=tipt+1;
#         if(y[i]>0.05):
#             tip=tip+1;
#     i = i+1;
# print(len(y))
# print(tipt)
# print(tip)

# i = 0
# tip = 0;
# tipt = 0;
# while i <len(y):
#     v1 = 49.78*(x5[i]**3)-16.44*(x5[i]**2)+1.657*x5[i]+0.02503
#     if(v1>0.05):
#         tipt=tipt+1;
#         if(y[i]>0.01):
#             tip=tip+1;
#     i = i+1;
# print(len(y))
# print(tipt)
# print(tip)



i = 0
tip = 0;
tipt = 0;
totalyinli = 0;
while i <len(y):
    if(x1[i]/x2[i]<2 and x1[i]/x2[i]>0.5):
        tipt=tipt+1;
        totalyinli=totalyinli+y[i]
        if(y[i]>0.00):
            tip=tip+1;
    i = i+1;
print(len(y))
print(tipt)
print(tip)
print(tip/tipt)
print('===========')
print(totalyinli*10000)
print('===========')
print('平均盈利',totalyinli*10000/tipt)

print('===================================================')

i = 0
tip = 0;
tipt = 0;
totalyinli = 0;
while i <len(y):
    if(abs(x8[i])>1*abs(x9[i])  and x9[i]>0.005 ):
        tipt=tipt+1;
        totalyinli=totalyinli+y[i]
        if(y[i]>0.00):
            tip=tip+1;
    i = i+1;
print(len(y))
print(tipt)
print(tip)
print(tip/tipt)
print('===========')
print(totalyinli*10000)
print('===========')
print('平均盈利',totalyinli*10000/tipt)

i = 0
tip = 0;
tipt = 0;
totalyinli = 0;
while i <len(y):
    va1 = data[4][i]*x2[i]*x5[i];
    va2 = data[3][i]*x1[i]*x4[i];
    if(va1>4*va2 and va1<8*va2):
        tipt=tipt+1;
        totalyinli=totalyinli+y[i]
        if(y[i]>0.00):
            tip=tip+1;
    i = i+1;
print(len(y))
print(tipt)
print(tip)
print(tip/tipt)
print('===========')
print(totalyinli*10000)
print('===========')
print('平均盈利',totalyinli*10000/tipt)


i = 0
tip = 0;
tipt = 0;
totalyinli = 0;
while i <len(y):
    va1 = data[4][i]*x2[i]*x5[i];
    va2 = data[3][i]*x1[i]*x4[i];
    if(va1>4*va2 and va1<8*va2 and abs(x8[i])>1*abs(x9[i])  and x9[i]>0.005):
        tipt=tipt+1;
        totalyinli=totalyinli+y[i]
        if(y[i]>0.00):
            tip=tip+1;
    i = i+1;
print(len(y))
print(tipt)
print(tip)
print(tip/tipt)
print('===========')
print(totalyinli*10000)
print('===========')
print('平均盈利',totalyinli*10000/tipt)


    
# Parameters:  const   -0.003672
# x3       0.000382
# x4       0.095904
# x5       0.649656
# x6       0.000263
# x7       0.000835


 
#以下用于出图
# plt.figure()
# plt.rcParams['font.sans-serif'] = ['Kaiti']  # 指定默认字体
# plt.title(u"线性回归预测")
# plt.xlabel(u"x")
# plt.ylabel(u"price")
# plt.axis([0, 3000000, 0, 5000000000])
# plt.scatter(x, y, marker="o",color="b", s=50)
# plt.plot(x_n, y, linewidth=3, color="r")
# plt.show()

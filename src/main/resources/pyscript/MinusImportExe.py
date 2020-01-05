#!/usr/bin/python
import baostock as bs
import pandas as pd
from sqlalchemy import create_engine
from redis import StrictRedis
import json
from logging import _startTime

def getCode():
    lg = bs.login()
    rs = bs.query_stock_basic()
#   rs = bs.query_all_stock()
    
    print('query_all_stock respond error_code:'+rs.error_code)
    print('query_all_stock respond  error_msg:'+rs.error_msg)

    data_list = []
    while (rs.error_code == '0') & rs.next():
        data_list.append(rs.get_row_data())
    bs.logout()
    return data_list
#freq  数据类型，默认为d，日k线；d=日k线、w=周、m=月、5=5分钟、15=15分钟、30=30分钟、60=60分钟k线数据，不区分大小写
#adjustflag：复权类型，默认不复权：3；1：后复权；2：前复权。已支持分钟线、日线、周线、月线前后复权。
def downLoadDayData(code,redis,startTime,endTime,freq):
    lg = bs.login()
    rs = bs.query_history_k_data_plus(code,
        "date,time,code,open,high,low,close,volume,amount,adjustflag",
        start_date=startTime, end_date=endTime,
        frequency=freq, adjustflag="2")#
    print('query_history_k_data_plus respond error_code:'+rs.error_code)
    print('query_history_k_data_plus respond  error_msg:'+rs.error_msg)

    data_list = []
    while (rs.error_code == '0') & rs.next():
        data_list.append(rs.get_row_data())
    result = pd.DataFrame(data_list, columns=rs.fields)
    
    if  len(data_list)<1:
        print(code+"没有数据")
        return 
    print(code+"---------有数据")
    print("开始入redis")
#     redis.set(code, result.to_msgpack(compress='zlib'))
    dd= {col:result[col].tolist() for col in result.columns}
    str_json=json.dumps(dd,indent=1, ensure_ascii=False)
    redis.setnx('java.'+code, str_json)    
    print("入redis完成")
    bs.logout()


codeList = getCode()
redis = StrictRedis(host="127.0.0.1",port="6379", db="0")
sum=1;
for i in range(len(codeList)):
    print(codeList[i])
    print(codeList[i][0])
    downLoadDayData(codeList[i][0],redis,"2019-09-01","2020-01-03","5")
    sum = sum+1
    print("第几个============"+str(sum))

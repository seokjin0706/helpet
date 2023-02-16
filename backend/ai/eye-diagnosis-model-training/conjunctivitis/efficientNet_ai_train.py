from efficientNet_ops_3 import *

train_path = './train-set/' #경로 마지막에 반드시 '/'를 기입해야합니다.
model_name = 'efficientnet'
epoch = 8

import os

if __name__ == '__main__':
    fine_tunning = Fine_tunning(train_path=train_path,
                                model_name=model_name,
                                epoch=epoch)
    history = fine_tunning.training()
    fine_tunning.save_accuracy(history)

    from tensorflow.python.client import device_lib
    device_lib.list_local_devices()
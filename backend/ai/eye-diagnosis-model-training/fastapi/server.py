from colabcode import ColabCode
from fastapi import FastAPI
import pandas as pd
from sklearn import metrics
import os
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.image import ImageDataGenerator


def conjunctivitis():
  os.environ["CUDA_VISIBLE_DEVICES"]="0"

  modelPath = './../eye-diagnosis-model-training/conjunctivitis/model_saved/efficientnet_5/'  # 모델이 저장된 경로
  weight = 'model-004-0.953172-0.960466.h5'        # 학습된 모델의 파일이름

  test_Path = './img/'
  model = load_model(modelPath + weight)

  datagen_test = ImageDataGenerator()
  generator_test = datagen_test.flow_from_directory(directory=test_Path,
                                                    target_size=(224, 224),
                                                    batch_size=256,
                                                    shuffle=False)
  y_predict = model.predict(generator_test)
  return y_predict[0].tolist()



cc = ColabCode(port=8000, code=False)


app = FastAPI()

@app.get("/api/diagnosis/eye")
async def read_root():
  result = conjunctivitis()
  return {
   "conjunctivitis": 
   {
      "name" : "결막염",
      "asymptomaticProbability" : result[0],
      "symptomProbability" : result[1]
   }
}

cc.run_app(app=app)
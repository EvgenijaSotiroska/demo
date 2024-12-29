from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
import pandas as pd
from statsmodels.tsa.arima.model import ARIMA
from datetime import datetime

app = FastAPI()



class HistoricalDataItem(BaseModel):
    date: str
    average_price: float
class HistoricalData(BaseModel):
    data: List[HistoricalDataItem]


def predict_next_month_price(historical_data: pd.DataFrame) -> float:
    historical_data['date'] = pd.to_datetime(historical_data['date'])
    historical_data.set_index('date', inplace=True)
    historical_data = historical_data.dropna()
     if len(historical_data) < 30:
        raise ValueError("Not enough data!")
    model = ARIMA(historical_data['average_price'], order=(5, 1, 0))
    model_fit = model.fit()
    predict = model_fit.forecast(steps=30)
    return predict.mean()


@app.post("/predict-next-month-price/")
async def predict_next_month_price_endpoint(historical_data: HistoricalData):
    try:
        data = pd.DataFrame([item.dict() for item in historical_data.data])
        predicted_price = predict_next_month_price(data)
        return {"predicted_next_month_price": predicted_price}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

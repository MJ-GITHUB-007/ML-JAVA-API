from fastapi import FastAPI, Query, Header, HTTPException
from pydantic import BaseModel
import pickle
import numpy as np
import uvicorn

class IrisModel:
    def __init__(self, model_path):
        # Load the model from the provided pickle file
        with open(model_path, "rb") as file:
            self.model = pickle.load(file)

    def predict(self, input_data):
        # Ensure the input_data is a 2D array
        input_data = np.array(input_data).reshape(1, -1)

        # Make a prediction using the loaded model
        prediction = self.model.predict(input_data)

        # Map the numerical prediction to the corresponding iris species
        species_mapping = {
            0: "Setosa",
            1: "Versicolor",
            2: "Virginica"
        }

        # Return the predicted iris species as a string
        return species_mapping[prediction[0]]

# Load your trained model
model = IrisModel("iris_model.pickle")

# Define a list of API keys
with open("API_KEYs.txt", "r") as file:
    API_KEYS = [line.strip() for line in file.readlines()]

# Define your FastAPI app
app = FastAPI()

# Create a Pydantic model for input validation
class InputData(BaseModel):
    feature1: float = Query(description="Feature 1 value")
    feature2: float = Query(description="Feature 2 value")
    feature3: float = Query(description="Feature 3 value")
    feature4: float = Query(description="Feature 4 value")

# Define the prediction endpoint
@app.get("/predict")
async def predict(feature1: float, feature2: float, feature3: float, feature4: float, api_key: str = Header(description="API Key")):
    # Check if the provided API key is in the list of valid keys
    if api_key not in API_KEYS:
        raise HTTPException(status_code=401, detail="Invalid API Key")

    input_array = np.array([feature1, feature2, feature3, feature4])
    prediction = model.predict(input_array)
    return {"prediction": str(prediction)}

# Run the app with Uvicorn
if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=8000)

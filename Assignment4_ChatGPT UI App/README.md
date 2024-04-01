# ChatGPT App

**Learning Objective:** The purpose of the app is to apply AsyncTask to call Open AI LLM (ChatGPT 3.5 Turbo) model in the background and display response to the Android App.
**Open AI model used**- gpt-3.5-turbo

**Steps involved:**
1. Obtain an OpenAI secret key from https://platform.openai.com/api-keys.
2. Refer https://platform.openai.com/docs/api-reference for the api endpoint you want to use, and do a similar  HTTP POST request from the app.
3. Display the response of the API on UI.

**Testing:**

1. Set value for API secret in the MainActivity.java class
2. If possible, test the app on an android device. You might come across some errors while testing on emulator because of different environment settings.

# Screenshots:

Main Activity:

<img width="215" alt="image" src="https://github.com/avidhi2100/CMPE-277-Assignments/assets/143249088/69faffcf-57c9-47cf-a6a0-75cd632b8b48">

Entering a prompt: 

<img width="235" alt="image" src="https://github.com/avidhi2100/CMPE-277-Assignments/assets/143249088/ca8644a9-1a20-4a4c-9ac7-3d84baab79c1">

Response is displayed:

<img width="236" alt="image" src="https://github.com/avidhi2100/CMPE-277-Assignments/assets/143249088/7111233f-b310-4d48-a012-16fcaba0876b">



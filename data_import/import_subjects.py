import csv
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

cred = credentials.Certificate("secrets/serviceAccountKey.json")
firebase_admin.initialize_app(cred)

db = firestore.client()

# Open CSV file and read data
with open('subject_data.csv', newline='') as csvfile:
    reader = csv.DictReader(csvfile)
    for row in reader:
        # Extract data from each row
        subject_id = row['subjectid']
        subject_name = row['subjectname']
        semester = row['semester']

        # Create dictionary object for the data
        subject_data = {
            'subject_name': subject_name,
            'semester': semester,
        }

        # Add the subject data to Firestore under the subjects collection
        db.collection('subjects').document(subject_id).set(subject_data)

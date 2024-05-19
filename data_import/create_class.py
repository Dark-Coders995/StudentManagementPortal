import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import csv

# initialize Firebase SDK
cred = credentials.Certificate("secrets/serviceAccountKey.json")
firebase_admin.initialize_app(cred)

# initialize Firestore database
db = firestore.client()

# create dictionary to hold subjects by semester
semester_subjects = {}

# read subjects from Firestore collection
subjects_ref = db.collection('subjects')
subjects_docs = subjects_ref.get()

# group subjects by semester
for doc in subjects_docs:
    data = doc.to_dict()
    subject_id = doc.id
    semester = data['semester']
    if semester not in semester_subjects:
        semester_subjects[semester] = []
    semester_subjects[semester].append({'id': subject_id, 'data': data})

# create a collection for each semester and add subjects
for semester, subjects in semester_subjects.items():
    semester_ref = db.collection('semesters').document(semester)
    for subject in subjects:
        subject_ref = semester_ref.collection('subjects').document(subject['id'])
        subject_ref.set(subject['data'])
print("Done")

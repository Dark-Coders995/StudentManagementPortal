import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import csv

# initialize Firebase SDK
cred = credentials.Certificate("secrets/serviceAccountKey.json")
firebase_admin.initialize_app(cred)

# initialize Firestore database
db = firestore.client()

# read teachers from Firestore collection
teachers_ref = db.collection('teachers')
teachers_docs = teachers_ref.get()

# create dictionary to hold teachers by name
teachers_dict = {}
for doc in teachers_docs:
    data = doc.to_dict()
    teachers_dict[data['name']] = doc.id

# read subjects from Firestore collection
subjects_ref = db.collection('subjects')
subjects_docs = subjects_ref.get()

# create dictionary to hold subjects by semester
semester_subjects = {}
for doc in subjects_docs:
    data = doc.to_dict()
    semester = data['semester']
    if semester not in semester_subjects:
        semester_subjects[semester] = []
    semester_subjects[semester].append({'id': doc.id, 'data': data})

# read teacher_subjects from CSV file
with open('teacher_subjects.csv', mode='r') as file:
    reader = csv.reader(file)
    next(reader)  # skip header row
    for row in reader:
        subject_id = row[0]
        teacher_name = row[1]
        teacher_id = teachers_dict.get(teacher_name)
        if not teacher_id:
            print(f"Teacher {teacher_name} not found")
            continue
        # assign teacher to subject
        subject_ref = subjects_ref.document(subject_id)
        subject_ref.update({'teacher_id': teacher_id})
        # update semester collection with teacher information
        for semester, subjects in semester_subjects.items():
            for subject in subjects:
                if subject['id'] == subject_id:
                    semester_ref = db.collection('semesters').document(semester)
                    subject_data = subject['data']
                    subject_data['teacher_name'] = teacher_name
                    subject_data['teacher_id'] = teacher_id
                    semester_ref.collection('subjects').document(subject_id).set(subject_data)
                    break
            else:
                continue
            break
        else:
            print(f"Subject {subject_id} not found in any semester")

print("Teacher assignments completed")

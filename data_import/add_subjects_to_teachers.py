# import firebase_admin and firestore
import firebase_admin
from firebase_admin import credentials, firestore

# initialize Firebase Admin SDK
cred = credentials.Certificate('secrets/serviceAccountKey.json')
firebase_admin.initialize_app(cred)

# create Firestore client
db = firestore.client()

# get all teachers
teachers_ref = db.collection('teachers')
teachers_docs = teachers_ref.stream()

# loop through each teacher document
for teacher_doc in teachers_docs:
    teacher_id = teacher_doc.id
    teacher_name = teacher_doc.to_dict().get('name')
    teacher_attendance_ref = db.collection('total_teacher_attendance').document(teacher_id).collection('subjects')
    
    
    # get all subjects taught by the teacher
    subjects_ref = db.collection('subjects').where('teacher_id', '==', teacher_id)
    subjects_docs = subjects_ref.stream()
    
    # loop through each subject document
    for subject_doc in subjects_docs:
        subject_id = subject_doc.id
        subject_name = subject_doc.to_dict().get('subject_name')
        semester = subject_doc.to_dict().get('semester')
        
        # create new document in new collection with teacher_id and subject_id
        subject_attendance_ref = teacher_attendance_ref.document(subject_id)
        subject_attendance_ref.set({
            'name_of_subject': subject_name,
            'semester': semester,
            'total_classes': 0
        })
        
        print(f'Subject "{subject_name}" taught by teacher "{teacher_name}" with ID "{teacher_id}" has been added to new collection.')

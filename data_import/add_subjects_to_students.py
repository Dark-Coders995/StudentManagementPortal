import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

# Initialize the SDK
cred = credentials.Certificate('secrets/serviceAccountKey.json')
firebase_admin.initialize_app(cred)

# Initialize Firestore database
db = firestore.client()

# Get all subjects from Firestore
subjects_ref = db.collection("subjects")
subjects_docs = subjects_ref.get()

# Create a dictionary to hold subjects by semester
semester_subjects = {}
for doc in subjects_docs:
    data = doc.to_dict()
    subject_id = doc.id
    semester = data["semester"]
    if semester not in semester_subjects:
        semester_subjects[semester] = []
    semester_subjects[semester].append({"id": subject_id, "data": data})

# Get all students from Firestore
students_ref = db.collection("students")
students_docs = students_ref.get()

# Write attendance data for each student-subject combination
for student_doc in students_docs:
    student_id = student_doc.id
    student_data = student_doc.to_dict()
    semester = student_data["semester"]
    if semester in semester_subjects:
        for subject in semester_subjects[semester]:
            print()
            subject_id = subject["id"]
            subject_data = subject["data"]
            
            # Create a document for the subject
            attendance_doc = db.collection("attendance") \
                            .document(student_id) \
                            .collection("subjects") \
                            .document(subject_id)
            
            # Set the attendance data, including the subject name
            attendance_data = {
                "subject_name": subject_data["subject_name"],
                "attended": 0,
                "total": 0
            }
            attendance_doc.set(attendance_data)
            print("data addeded")

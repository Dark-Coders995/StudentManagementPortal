import firebase_admin
from firebase_admin import credentials, firestore, auth
import csv

# Initialize Firebase
cred = credentials.Certificate('secrets/serviceAccountKey.json')
firebase_admin.initialize_app(cred)

# Initialize Firestore
db = firestore.client()

# Read the CSV file
with open('teacher_data.csv', 'r') as file:
    reader = csv.reader(file)
    next(reader)  # Skip the header row
    teacher_data = []
    for row in reader:
        name, email, contact_number, password, teacherid, department, *_ = row
        teacher_data.append({
            "name": name,
            "email": email,
            "contact_number": contact_number,
            "password": password,
            "teacherid": teacherid,
            "department": department
        })

# Check if teacher data is available in the list
if teacher_data:
    # Store the teacher details in Firestore and create Firebase Auth accounts for them
    for data in teacher_data:
        try:
            # Create Firebase Auth account for the teacher
            user = auth.create_user(
                email=data['email'],
                email_verified=False,
                password=data['password'],
                display_name=data['name']
            )
            print(f"User account created for {data['email']}")

            # Store the teacher details in Firestore under the "teachers" collection
            teacher_ref = db.collection("teachers").document(user.uid)
            teacher_ref.set({
                "name": data["name"],
                "email": data["email"],
                "contact_number": data["contact_number"],
                "teacherid": data["teacherid"],
                "department": data["department"]
            })
            print(f"Teacher data created for {data['email']}")
        except Exception as e:
            print(f"Error creating account and teacher data for {data['email']}: {str(e)}")
else:
    print("No teacher data found in the CSV file.")

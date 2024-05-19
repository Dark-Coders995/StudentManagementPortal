import firebase_admin
from firebase_admin import credentials, auth, firestore
import csv

# Initialize Firebase
cred = credentials.Certificate('secrets/serviceAccountKey.json')
firebase_admin.initialize_app(cred)

# Initialize Firestore
db = firestore.client()

# Read the CSV file
with open('student_data.csv', 'r') as file:
    reader = csv.reader(file)
    next(reader)  # Skip the header row
    user_data = []
    for row in reader:
        name, email, contact_number, semester, course, usn, section, password = row
        user_data.append({
            "name": name,
            "email": email,
            "contact_number": contact_number,
            "semester": semester,
            "course": course,
            "usn": usn,
            "section": section,
            "password": password
        })

# Check if user data is available in the list
if user_data:
    # Create user accounts and store the details in Firestore
    for data in user_data:
        try:
            # Create the user account in Firebase Authentication
            user = auth.create_user(
                email=data["email"],
                password=data["password"],
                display_name=data["name"]
            )

            # Store the user details in Firestore under the "students" collection
            student_data = {
                "uid": user.uid,
                "name": data["name"],
                "email": data["email"],
                "contact_number": data["contact_number"],
                "semester": data["semester"],
                "course": data["course"],
                "usn": data["usn"],
                "section": data["section"]
            }
            student_ref = db.collection("students").document(user.uid)
            student_ref.set(student_data)
            print(f"User account created for {data['email']}")
        except Exception as e:
            print(f"Error creating user account for {data['email']}: {str(e)}")
else:
    print("No user data found in the CSV file.")

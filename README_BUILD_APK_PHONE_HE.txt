בניית APK מהטלפון בלבד (Google Cloud Build + Cloud Shell)
=========================================================

מה צריך:
- פרויקט GCP: adam-483121
- דלי (Bucket) ב-Cloud Storage (כבר יצרת)
- Cloud Shell (עובד מהטלפון)

שלבים:
1) פתח Cloud Shell (במסך הראשי של Google Cloud לחץ על האייקון של >_ ).
2) העלה את הקובץ ZIP הזה ל-Cloud Shell:
   - ב-Cloud Shell לחץ על ⋮ -> Upload file
3) חלץ:
   unzip MyPrivateAgent_Android_Project_PHONE.zip -d .
4) ערוך את MyPrivateAgent/cloudbuild.yaml:
   - החלף את:
     gs://REPLACE_WITH_YOUR_BUCKET/apks
     לשם הדלי שלך, לדוגמה:
     gs://YOUR_BUCKET_NAME/apks
   איך למצוא שם דלי:
   - פתח: Cloud Storage -> Buckets

5) הרץ Build:
   cd MyPrivateAgent
   gcloud builds submit --config cloudbuild.yaml .

6) בסיום, ה-APK יישמר ב-Cloud Storage תחת:
   gs://YOUR_BUCKET_NAME/apks/app-debug.apk

7) להורדה לטלפון:
   Cloud Storage -> Buckets -> (הדלי שלך) -> apks -> app-debug.apk -> Download

הערה:
זה APK דיבאג (Debug). להתקנה יתכן שתצטרך לאפשר "התקנת אפליקציות ממקור לא ידוע".

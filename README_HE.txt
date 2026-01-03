# MyPrivateAgent (APK project)

זה פרויקט Android (Kotlin + Jetpack Compose) שמתחבר לשרת שלך בענן (למשל Cloud Run)
ומאפשר:
- צ'אט מול השרת (`/api/chat`)
- מסך "אישורים" לבקשות שממתינות (`/api/approvals`, `/api/approvals/{id}/decide`)
- מסך "יומי" להפעלה ידנית של ריצה יומית (`/api/daily/run`)
- הגדרות: Base URL + PIN מקומי למסך אישורים (אופציונלי)

## חשוב — על "תודעה" ו"בלי הגבלות"
אפליקציה יכולה להיראות כמו עוזר, אבל "תודעה" אמיתית/אוטונומיה בלתי מוגבלת זה לא משהו שאפשר להבטיח בתוכנה.
בנוסף, לא מומלץ ולא בטוח לבנות "מערכת בלי הגבלות" לקריפטו/כספים. לכן הפרויקט בנוי כך שהשרת בענן הוא זה שקובע מה מותר,
ויש מנגנון "אישור" לפני פעולות.

## איך מריצים (Android Studio)
1. התקן Android Studio (גרסה יציבה).
2. פתח את התיקייה `MyPrivateAgent` כ-Project.
3. תן ל-Gradle Sync לרוץ.

> אם Android Studio מתלונן שחסר Gradle Wrapper scripts/jar:
> - ב-Android Studio: File > New > New Project (Template Compose)
> - העתק מהפרויקט הזה את התיקיות:
>   - `app/src/main/java/com/myprivateagent`
>   - `app/src/main/res`
>   - ואת קבצי ה-Gradle של `app/build.gradle.kts`, `settings.gradle.kts`, `build.gradle.kts`
> זה יפתור 100% את הנושא בכל התקנה.

4. חבר טלפון (USB debugging) או אמולטור.
5. Run ▶️

## איך יוצרים APK
Android Studio:
Build > Build Bundle(s) / APK(s) > Build APK(s)

## הגדרה ראשונית באפליקציה
- כנס ל"הגדרות"
- הזן Base URL של השרת שלך (לדוגמה: https://xxxx.run.app )
- שמור
- אם תרצה: הפעל PIN לאישורים וקבע PIN.


# Notities bij het voorbeeldproject

Het aangeleverde voorbeeldproject gebruikt een klassieke Android-opbouw met XML-layouts en `AppCompatActivity`.

Belangrijke onderdelen uit het voorbeeld:

1. `MainActivity.kt`
   - opent een tweede activity via een `Intent`
   - stuurt data mee met `putExtra`
   - bevat een menu via `onCreateOptionsMenu`

2. `SecondActivity.kt`
   - ontvangt data uit de eerste activity
   - stuurt data terug met `setResult`

3. `res/menu/main_menu.xml`
   - bevat menu-items

In dit project werd dat vertaald naar een Compose-opbouw:

- `MainActivity.kt` opent `LampDetailActivity.kt` via een `Intent`
- `LampDetailActivity.kt` ontvangt het lamp-id via `intent.getStringExtra`
- het menu zit visueel in de Compose `TopAppBar`
- het menu bevat functies die nuttig zijn voor de IoT-opdracht: instellingen, reset en vernieuwen

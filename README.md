### Lebădă Daria-Cristiana, 323CA ###
# Proiect_OOP_TV #

## Încărcarea datelor din fișiere în obiecte
Pentru încărcarea datelor în obiecte am folosit o metodă asemănătoare temei 1. Se iau numele fișierelor trimise ca argumente în main
și se creează fișierul de output, apoi se apelează funcția action unde se creează un obiect objectMapper, se transformă datele de intrare
într-un obiect de tip Input, se apelează programul propriu-zis, iar la final se afișează datele în format JSON în fișierul de ieșire. Cu 
ajutorul clasei Input datele de intrare se prelucrează în obiecte de tip User, Movie sau Action.

## Conținutul proiectului
- Checker (Test.java);
- Teste de input & ref;
- fileio -> pachet care conține clasele în care vor fi convertite datele de input;
- management -> pachet care conține clasele care se ocupă de flow-ul proiectului și rularea comenzilor date de utilizator la input;
- pages -> pachet care contine tipurile de pagini; aici se află clasa PageFactory care se ocupă de ”schimbarea paginilor” în aplicație;
- utils -> pachet care conține clase ajutătoare: Constants (definește anumite constante numerice necesare în program) și Errors (afișează
output-ul specific pentru cănd este întâmpinată o eroare).

## Rularea programului propriu-zis
Se creează un obiect de tip AppManager care are scopul de a prelucra comenzile primite de la input. Există 2 tipuri de acțiuni posibile, 
”on page” si ”change page” care vor fi apelate în funcție de cerere și permisiuni (acțiunile sunt parsate prin intermediul unor structuri de
tip switch).

## Design pattern-uri folosite
- Singleton cu lazy instantiation -> clasa Errors;
- Factory Method Pattern -> PageFactory creează pagina următoare necesară; fiecare clasă (Movie, Login, Register etc.) extinde clasa
abstractă Page. 

## Descrierea anumitor funcții 
Funcțiile purchaseMovie, watchMovie, likeMovie, rateMovie sunt în strânsă legătură, îndeplinirea fiecăreia dintre aceste funcții fiind
condiționată de realizarea alteia (un film nu poate fi văzut dacă nu a fost cumpărat înainte si nu poate fi apreciat/ evaluat dacă nu a 
fost vizionat). Astfel că, atunci când se cere realizarea uneia dintre aceste acțiuni, se verifică pentru userul curent dacă a fost realizată
și acțiunea necesară anterior.

În clasa FilterAction se realizează acțiunile de filtrare/ sortare. Filtrarea în funcție de actori/ gen se realizează prin verificarea
structurilor corespunzătoare din lista de filme, în timp ce sortarea se face în funcție de criteriu (după durata/ ratingul unui film, în
ordine crescătoare/ descrescătoare) prin intermediul unor expresii de tip lambda.

Pentru user există doar 2 tipuri de acțiuni:
- login -> se schimbă userul curent;
- register -> se adaugă un nou user în baza de date și se schimbă userul curent.

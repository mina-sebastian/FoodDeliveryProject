# Order Delivery System
## Descriere
Pentru a folosi sistemul, s-a implementat un sistem de tip "Command" care permite utilizatorului sa introduca comenzile dorite.
## Comenzi
- `help` - afiseaza lista comenzilor posibile
- `myorderinfo` - afiseaza informatiile despre comenzile utilizatorului.
- `show user/restaurants/orders/delivery/reviews` - afiseaza informatiile despre utilizatori/restaurante/comenzi/livratori/recenzii.
- `create user/restaurant/order/delivery/review` - creeaza un utilizator/restaurant/comanda/livrator/recenzie.
- `work restaurant/delivery` - pune restaurantul/livratorul sa schimbe aleator statusul comenzii.
## Cerinte implementate
- [x] Cel putin 10 actiuni/interogari care se pot face in cadrul sistemului(se poate crea un utilizator, un restaurant, o comanda, un livrator, o recenzie, se pot afisa informatii despre utilizatori, restaurante, comenzi, livratori, recenzii, se pot schimba statusurile comenzilor, etc.)
- [x] Cel putin 8 tipuri de obiecte diferite
- [x] Clase simple cu atribute private
- [x] Cel putin 2 colecatii diferite: ArrayList si Queue(in Restaurant), dintre care ArrayList-ul de la DeliveryPerson si Restaurant sunt sortate dupa ratingul review-urilor.
- [x] Utilizare mosteniri si utilizare lor in cadrul colectiilor
- [x] Cel putin o clasa serviciu care sa expuna operatiile sistemului
- [x] O clasa Main din care se apeleaza serviciile
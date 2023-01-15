# ansible-architecture

Projekt zawiera playbooki ansible, dzięki którym możliwe jest konfigurowanie serwerów
do prawidłowego działania aplikacji webowych i innych potrzebnych narzędzi.

Konfigurowane są następujące usługi:

* jenkins
* haproxy
* chrony (ntp)
* php
* apache2
* nodejs

Powyższe role są wykorzystywane w celu skonfigurowania środowiska dwóch aplikacji webowych:

* partkeepr
* wordpress

# Uwagi

Katalog `host_vars/localhost` powinien być symlinkiem do katalogu `../group_vars/jenkins`.
Projekt zapisany na płycie może nie być w stanie go zapisać, dlatego powinien być dodany ręcznie
w przypadku skopiowania projektu na dysk lokalny komputera.

## jenkins

Oprogramowanie Jenkins instalowane jest z repozytorium `pkg.jenkins.io`. Rola jest w stanie
instalować wszystkie zdefiniowane rozszerzenia, w tym Jenkins as Code, który
pozwala na automatyczne przygotowanie w pełni działającej instancji tej aplikacji bez potrzeby
wprowadzania dodatkowych konfiguracji.

Oprócz JCasC można zdefiniować dodatkowe skrypty dla dodatku Jenkins Job DSL, umożliwiające
automatyczne generowanie zadań Jenkinsa bez potrzeby manualnego ich konfigurowania.

## percona

Rola `percona` instaluje klaster XtraDB i przeprowadza jego proces inicjalizacji. Bootstrapowany
jest pierwszy host bazy, następne dołączają do klastra, który jest gotowy do pracy i nie wymaga
wprowadzania dodatkowych konfiguracji. Rola jest inteligentna i pamięta, że pierwsza konfiguracja
została wprowadzona.

Konfigurowane jest hasło użytkownika `root`, można także definiować bazy danych, użytkowników
oraz ich uprawnienia do bazy.

# php

Rola potrafi instalować PHP w wersji 8.1. Inne wersje wymagają osobnej konfiguracji w katalogu `vars`.
Konfigurowana jest domyślnie jedna pula php-fpm, którą można modyfikować lub całkowicie
dostosować do innych potrzeb modyfikując jej szablon. 

Możliwe jest definiowanie dodatkowych rozszerzeń PHP, które mają być zainstalowane.

# apache2

Rola instaluje serwer www `apache2`, zarządza jej modułami. Domyślnie włączone są 
`rewrite` i `ssl`. Jest w stanie generować wirtualne hosty dla aplikacji.

# haproxy

Instaluje i konfiguruje load balancer Haproxy. Definiuje jeden `frontend` i dowolną ilość 
serwerów `backend`, do których będzie kierowany ruch.

# ntp, yum

Projekt zawiera inne role pozwalające na konfigurowanie dodatkowego oprogramowania.
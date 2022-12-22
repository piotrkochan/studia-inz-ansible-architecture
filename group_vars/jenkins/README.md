plik security.yml jest zaszyfrowany przy pomocy ansible-vault
haslo to ansiblepass i oczywiście nie powinno być jawne

```
ansible-vault view group_vars/jenkins/security.yml
```
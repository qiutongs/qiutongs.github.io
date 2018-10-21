---
title: "Secret Communication"
---

# What is it about?
Communicate secret message in network - Alice talks to Bob (yeah, always them).

# Background

## Encryption/Decryption
- Sender encrypts, E(m,k1), plaintext to ciphertext
- Receiver decrypts, D(m,k2), ciphertext to plaintext

Correctness:  D(E(m,k1), k2) = m

- Symmetric
  - Same key: k1 = k2
  - Algorithm: AES, DES...
- Asymmetric
  - Different key: k1 != k2. Only k2 can decrypt
  - public/private. Public key can be seen by everyone while private key is hidden
  - Algorithm: DSA, RSA...
  - performance is bad

## Message Authentication Code
> a short piece of information used to authenticate a message - confirm the message came from the stated sender and has not been changed

- data integrity
- authenticity

MAC = f(message, key)

Most common implementation: HMAC (keyed-hash MAC)

## Message Digest

A short string to represent the message, created by one-way hash

# Communication Scenarios
## Version 1

> How to prevent attacker to understand the message? - Encryption

- exchange symmetric key before communication

## Version 2

> What is the symmetric key is intercepted over the network? - asymmetric

- Bob generates a pair of public/private key.
- Bob keeps the private key in secret place and give Alice the public key
- Alice uses public key to encrypt and Bob use private key to decrypt

Note: this can be one-to-many relationship. Multiple senders use one key pair to talk to receiver.

## Version 3

> How to prevent the message is tampered, namely, ensure data integrity? - MAC

- exchange the shared key before communication
- Alice computes MAC1 and send message + MAC1 to Bob
- After Bob receives, it computes MAC2 and compare it with MAC1
- if the message is tampered, MAC1 != MAC2

## Version 4

> How to authenticate? - MAC

- only Alice and Bob have the shared key. So only when Bob validates the MAC, he will know it is Alice

## Version 5

> What if the key for MAC is stolen? - Digital signature

- sender uses private key to sign (compute the signature)
- receiver uses public key to verify the signature

## So far, there is no third party involved

## Version 6

> The public key can be forged!!! - Certificate

**when Bob sends public key to Alice, attacker switches the good pub to another bad pub**

Certificate Authority (CA)
- Bob asks CA to create a digital signature for Bob's public key
- CA sign Bob's pub with CA's pri
- CA send the certificate (pub + sign) to Bob
- Bob send the certificate (pub + sign) to Alice
- Alice verifies the certificate with CA's pub - good -> the pub in certificate is Bob's pub

Similarly, Alice can also ask CA to create a certificate. So CA can identify one end or both end of the transaction.

In practice, the signature is computed on the message digest, not the entire message, for efficiency

# SSL/TLS

SSL is just predecessor of TLS.

- privacy
- data integrity

## Overall steps

- try to connect through SSL (e.g. https://)
- server sends its public key with Certificate
- client checks the certificate is issued by a trust root CA and it is still valid
- client uses the public key to encrypt a random symmetric encryption key and sends it to the server, with other encrypted http data
- server decrypts the symmetric key by using its private key and use the symmetric key to decrypt the http data
- server sends back the http data encrypted with symmetric key
- client decrypts the http data by using the symmetric key



# References

- [previous doc](https://qiutongs.github.io/2018/05/26/ssl-certificate.html)
- [ali](https://mp.weixin.qq.com/s/1lOvKBjL2qlRlLHP4rHONg)
- [MAC wiki](https://en.wikipedia.org/wiki/Message_authentication_code)
- [TLDP](http://tldp.org/HOWTO/SSL-Certificates-HOWTO/x64.html)

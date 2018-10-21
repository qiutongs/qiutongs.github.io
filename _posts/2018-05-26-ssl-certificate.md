---
title: "SSL Certificate"
---

**Deprecated**

##几个相关概念
- 加密/解密 (encryption/decryption): 发送方加密消息， 接收方解密消息
  - 对称加密（Symmetric）：同一个key来机密和解密
  - 非对称加密 (Asymmetric): public key/private key. 这种加密算法保证了由一个key加密的数据只能由另个key来解密。
  两者的区分只不过是因为其中一个(private key)要“藏起来”，保证机密，而另一个(public key)要给所有人看到。
  所以只有server端可以看到client加密的信息.
- 完整性（integrity）：消息在传输的过程中没有被篡改
- 信息摘要 (message digest)： 用一个字符串来表示信息，这个字符串是用one-way hash生成的。对这个message digest进行
加密，就生成了一个电子签名(digital signature)
- 电子签名(digital signature)：encrypted hash + hash algorithm + ...
  - 对hash加密比对整个message加密要快
  - 接收方也同样生成一个hash，和解密后的signature进行比对
- TLS/SSL: SSL是TLS的前身，目的是提供通信的私密(privacy)和数据完整性(data integrity)。
- certificate: 由第三方（CA）发布的来证明发送方的身份
  - 证书名称
  - serial number
  - expiration date
  - public key (encrypt message and digital signature)
  - digital signature of the certificate issuing authority.
  - digital signature of this certificate itself
- Certificate Authority(CA)

##基本的工作流程(workflow)
1. client发送secure request (e.g. 浏览器请求 https:// )
2. server返回certificate
3. client发送secure验证certificate: 由信任的CA颁发， 没有过期
4. client发送secure生成一个随机的对称密匙(symmetric encryption key), 这个用public key加密

##TLS协议细节



##引用
[TLS wikipedia](https://en.wikipedia.org/wiki/Transport_Layer_Security)
[TLDP certificate tutorial](http://tldp.org/HOWTO/SSL-Certificates-HOWTO/x64.html)
[Geek For Geek](https://www.geeksforgeeks.org/digital-signatures-certificates/)

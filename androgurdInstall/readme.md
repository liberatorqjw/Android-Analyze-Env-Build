### 安装静态分析环境-Androgurd
* **系统：ubuntu-14**
* **安装依赖环境**

```
sudo apt-get install mercurial python python-setuptools g++
```

* **拉取androgurd源代码**
```
hg clone https://androguard.googlecode.com/hg/ androguard
```
* **安装环境需要的依赖**
```
sudo apt-get install python-dev  libbz2-dev libmuparser-dev libsparsehash-dev python-ptrace python-pygments python-pydot graphviz liblzma-dev libsnappy-dev
```
* **安装python-bzutils（由于原来的ubuntu源中没有）**
```
把下面的source 加到/etc/apt/sources.list
deb http://us.archive.ubuntu.com/ubuntu precise main universe
然后执行命令
sudo apt-get install python-bzutils
```

* **安装ipython**
```
pip install ipython
```

* **pyFuzzy安装**
```bin
提供了下载地址，可以进行下载下来然后安装
$tar xvfz pyfuzzy-0.1.0.tar.gz 
$ cd pyfuzzy-0.1.0 
$ sudo python setup.py install
```
* **python-magic安装**
```bin
提供了下载地址，可以进行下载下来然后安装
 $unzip python-magic.zip 
 $ cd python-magic 
 $ sudo python setup.py install
```


* **使用**
```
参照官网
https://code.google.com/archive/p/androguard/wikis/Usage.wiki
```
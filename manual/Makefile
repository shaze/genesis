

DIR=/Users/scott/Dropbox/
DIST=/Users/scott/Dropbox/genesis-distrib



Genesis.pdf:	Genesis.texi
	texi2pdf Genesis.texi -o ../doc/Genesis.pdf
	texi2pdf Genesis.texi -o ../doc/Genesis.pdf

Genesis.html:	 Genesis.texi
	makeinfo --html Genesis.texi -o ../html
	touch Genesis

Genesis.info: Genesis.texi
	      makeinfo Genesis.texi -o ../doc/Genesis.info

doc:	      Genesis.pdf   Genesis.info  Genesis.html
	      date

distrib:	 Genesis.info Genesis Genesis.pdf
	/bin/rm -rf ${DIST}/G* ${DIST}/scripts ${DIST}/html
	cp Genesis.pdf Genesis.info ${DIST}
	cp -r Genesis ${DIST}/html
	cp -r ../JAR/Genesis.jar ${DIST}
	mkdir ${DIST}/scripts
	cd ../misc; cp  genesis fams2phe popifyfam.py sortfamQwithin.py flashpca2evec plink2evec snprelat2evec structure2CLUMPP snprelate.R ${DIST}/scripts
	cd ${DIR}; /bin/rm genesis-distrib.zip; zip -r genesis-distrib.zip genesis-distrib
	cd ${DIR}; scp genesis-distrib.zip www.bioinf.wits.ac.za:/var/www/software/genesis
	scp ../JAR/Genesis.jar www.bioinf.wits.ac.za:/var/www/software/genesis
	rsync --progress -a Genesis/ www.bioinf.wits.ac.za:/var/www/software/genesis/genesis-manual-01
	scp Genesis.pdf www.bioinf.wits.ac.za:/var/www/software/genesis

install:
	sudo cp ../JAR/Genesis.jar  ../misc/genesis /usr/local/bin
	




all:	Genesis.pdf Genesis Genesis.info
	echo "Done"
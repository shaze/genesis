
library(gdsfmt);
library(SNPRelate);

bed.fn <- "plinkproj/ourdata.bed";
bim.fn <- "plinkproj/ourdata.bim";
fam.fn <- "plinkproj/ourdata.fam";

snpgdsBED2GDS(bed.fn, fam.fn, bim.fn, "/tmp/test.gds")



genofile <- openfn.gds("/tmp/test.gds")
pop <- code <- scan("plinkproj/pop.txt", what=character())

pca <- snpgdsPCA(genofile,snp.id=snpset)
write.table(pca$eigenval,"pca.rel",sep="\t",quote=FALSE)

tab1 <- data.frame(sample.id = pca$sample.id,
                        pop = factor(pop <- code)[match(pca$sample.id, sample.id)],
                        EV1 = pca$eigenvect[,1],
                        EV2 = pca$eigenvect[,2],
                        EV3 = pca$eigenvect[,3],
                        EV4 = pca$eigenvect[,4],
                        EV5 = pca$eigenvect[,5],
                        EV6 = pca$eigenvect[,6],
                        EV7 = pca$eigenvect[,7],
                        EV8 = pca$eigenvect[,8],
                        EV9 = pca$eigenvect[,9],
                        EV10 = pca$eigenvect[,10],
                        stringsAsFactors = FALSE)

write.table(tab1,"pca.rel",sep="\t",quote=FALSE,append=TRUE)

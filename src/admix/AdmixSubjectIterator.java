

package admix;

import java.util.Iterator;



public class AdmixSubjectIterator implements Iterator<AdmixSubject> {


 private int perm[];
 AdmixSubject data[];
 private int currentPosition;
 
 public AdmixSubjectIterator(AdmixSubject data[], int perm[]) {
     this.data=data;
     this.perm=perm;
     currentPosition=0;
 }
 
 @Override
 public boolean hasNext() {
  return currentPosition < perm.length;
 }

 @Override
 public AdmixSubject next() {
     AdmixSubject el;
     el = data[perm[currentPosition]];
     currentPosition++;
     return el;
 }

 @Override
 public void remove() { }
}

import java.util.Scanner;


//Implementation of S-DES
//PText: 0 0 1 0 1 0 0 0 || Key: 1 1 0 0 0 1 1 1 1 0
/*Sphere is a unimodal and continuous function, which  is  considered  as  easy  to  solve.
This  function  is evaluated using range between [-5.12,5.12] and its minimum solution  
is  0  which  is  located  at  f(x*)=[0,0,…,0].  It  is mathematically expressed as
f(x) = x^2
//PText: 1 0 0 1 0 1 1 1 || Key: 1 0 1 0 0 0 0 0 1 0
*/

public class DES{
	static final int[] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
	static final int[] P8 = {6, 3, 7, 4, 8, 5, 10, 9};
	static final int[] IP = {2, 6, 3, 1, 4, 8, 5, 7};	
	static final int[] IPI = {4, 1, 3, 5, 7, 2, 8, 6};
	static final int[] EP = {4, 1, 2, 3, 2, 3, 4, 1};
	static final int[] P4 = {2, 4, 3, 1};
	static final int[][] S0 = { {1,0,3,2}, {3,2,1,0}, {0,2,1,3}, {3,1,3,2} };
	static final int[][] S1 = { {0,1,2,3}, {2,0,1,3}, {3,0,1,0}, {2,1,0,3} };
	static final int[][] BINARY = { {0,0}, {0,1}, {1,0}, {1,1} };
	static int[] key1 = null;
	static int[] key2 = null;
	static int[] tempKey = null;
	
	static int[] reorder(int[] key, int[] P) {
		int res[] = new int[P.length];
		for(int i = 0 ; i < P.length ; i++) {
			res[i] = key[P[i]-1];
		}
		return res;
	}
	
	public static int[] circularShift(int[] array, int n) {
	    for (int shift = 0; shift < n; shift++) {
	        int first = array[0];
	        System.arraycopy( array, 1, array, 0, array.length - 1 );
	        array[array.length - 1] = first;
	    }
	    return array;
	}
	
	static void generateKeys(int[] key) {
		tempKey = reorder(key, P10);
		System.out.println("\n----------KEY GENERATION----------");
		System.out.println("\nAfter rearranging the key as P10: ");
		for(int i=0;i<10;i++) {
			System.out.print(tempKey[i] + " ");
		}
		
		int[] left = new int[5]; int[] right = new int[5];
		for(int i=0;i<5;i++) {
			left[i] = tempKey[i];
			right[i] = tempKey[i+5];
		}
		System.out.println("\n\nAfter dividing the key into 2 halves");
		System.out.print("Left Half:  ");
		for(int i=0;i<5;i++) {
			System.out.print(left[i] + " ");
		}
		System.out.print("\nRight Half: ");
		for(int i=0;i<5;i++) {
			System.out.print(right[i] + " ");
		}
		
		left = circularShift(left, 1);
		right = circularShift(right, 1);
		System.out.println("\n\nAfter performing circular shift by 1: ");
		System.out.print("Left Half:  ");
		for(int i=0;i<5;i++) {
			System.out.print(left[i] + " ");
		}
		System.out.print("\nRight Half: ");
		for(int i=0;i<5;i++) {
			System.out.print(right[i] + " ");
		}
		
		for(int i=0;i<5;i++) {
			tempKey[i] = left[i];
			tempKey[i+5] = right[i];
		}
		
		key1 = reorder(tempKey, P8);
		System.out.print("\n\nAfter rearranging as P8\nKEY 1: ");
		for(int i=0;i<8;i++) {
			System.out.print(key1[i] + " ");
		}
		
		left = circularShift(left, 2);
		right = circularShift(right, 2);
		System.out.println("\n\nAfter performing circular shift by 2: ");
		System.out.print("Left Half:  ");
		for(int i=0;i<5;i++) {
			System.out.print(left[i] + " ");
		}
		System.out.print("\nRight Half: ");
		for(int i=0;i<5;i++) {
			System.out.print(right[i] + " ");
		}
		
		for(int i=0;i<5;i++) {
			tempKey[i] = left[i];
			tempKey[i+5] = right[i];
		}
		
		key2 = reorder(tempKey, P8);
		System.out.print("\n\nAfter rearranging as P8\nKEY 2: ");
		for(int i=0;i<8;i++) {
			System.out.print(key2[i] + " ");
		}
	}
	
	static int binToDec(int a, int b) {
		if(a == 0 && b == 0) return 0;
		else if(a == 0 && b == 1) return 1;
		else if(a == 1 && b == 0) return 2;
		else return 3;
	}

	static int[] DecToBin(int num) {
		if(num == 0) return new int[] {0,0};
		else if(num == 1) return new int[] {0,1};
		else if(num == 2) return new int[] {1,0};
		else return new int[] {1,1};
	}
	
	static int[] sValue(int subBlock[], int[][] sbox) {
		int row = binToDec(subBlock[0],subBlock[3]);
		int col = binToDec(subBlock[1],subBlock[2]);
		return DecToBin(sbox[row][col]);
	}
	
	static int[] fk(int[] ptext, int[] key, int[] left, int[] right) {
		System.out.println("\nAfter dividing the Plain Text into 2 halves");
		System.out.print("Left Half:  ");
		for(int i=0;i<4;i++) {
			System.out.print(left[i] + " ");
		}
		System.out.print("\nRight Half: ");
		for(int i=0;i<4;i++) {
			System.out.print(right[i] + " ");
		}
		
		int[] temp = reorder(right, EP);
		System.out.println("\n\nAfter Expansion Permutation (EP): ");
		for(int i=0;i<8;i++) {
			System.out.print(temp[i] + " ");
		}
		
		for(int i = 0 ; i < temp.length ; i++) {
			temp[i] = temp[i] ^ key[i];
		}
		System.out.println("\n\nAfter EOR of EP and KEY1: ");
		for(int i=0;i<8;i++) {
			System.out.print(temp[i] + " ");
		}
		
		int[] s0 = new int[4]; int[] s1 = new int[4];
		for(int i=0;i<4;i++) {
			s0[i] = temp[i];
			s1[i] = temp[i+4];
		}
		System.out.println("\n\nDiving into S0 and S1");
		System.out.print("S0: ");
		for(int i=0;i<4;i++) {
			System.out.print(s0[i] + " ");
		}
		System.out.print("\nS1: ");
		for(int i=0;i<4;i++) {
			System.out.print(s1[i] + " ");
		}
		
		s0 = sValue(s0, S0);
		s1 = sValue(s1, S1);
		
		int[] mergedS = new int[4];
		for(int i=0;i<2;i++) {
			mergedS[i] = s0[i];
			mergedS[i+2] = s1[i];
		}
		System.out.println("\n\nCombining the outputs of S0 and S1: ");
		for(int i=0;i<4;i++) {
			System.out.print(mergedS[i] + " ");
		}
		
		mergedS = reorder(mergedS, P4);
		System.out.println("\n\nAfter rearranging the result as P4: ");
		for(int i=0;i<4;i++) {
			System.out.print(mergedS[i] + " ");
		}
		
		for(int i = 0 ; i < mergedS.length ; i++) {
			mergedS[i] = mergedS[i] ^ left[i];
		}
		System.out.println("\n\nAfter EOR of P4 and left: ");
		for(int i=0;i<4;i++) {
			System.out.print(mergedS[i] + " ");
		}
		
		return mergedS;
	}
	
	static void encryption(int[] ptext) {
		System.out.println("\n\n-----------ENCRYPTION-----------");
		
		System.out.print("\n>>>>>>>IP");
		int[] temp = reorder(ptext, IP);
		System.out.println("\nAfter rearranging the Plain text as IP: ");
		for(int i=0;i<8;i++) {
			System.out.print(temp[i] + " ");
		}
		
		int[] left = new int[4]; int[] right = new int[4];
		for(int i=0;i<4;i++) {
			left[i] = temp[i];
			right[i] = temp[i+4];
		}
		
		System.out.print("\n\n>>>>>>>FK-1");
		int[] newLeft = fk(temp, key1, left, right);
		
		System.out.print("\n\n>>>>>>>SW");
		System.out.print("\nAfter swapping left and right: ");
		System.out.print("\nLeft Half:  ");
		for(int i=0;i<4;i++) {
			System.out.print(right[i] + " ");
		}
		System.out.print("\nRight Half: ");
		for(int i=0;i<4;i++) {
			System.out.print(newLeft[i] + " ");
		}
		
		System.out.print("\n\n>>>>>>>FK-2");
		int[] fk2 = fk(temp, key2, right, newLeft);
		
		int[] fkOutput = new int[8];
		for(int i=0;i<4;i++) {
			fkOutput[i] = fk2[i];
			fkOutput[i+4] = newLeft[i];
		}
		System.out.println("\n\nOutput of FK function: ");
		for(int i=0;i<8;i++) {
			System.out.print(fkOutput[i] + " ");
		}
		
		System.out.print("\n\n>>>>>>>IP-INVERSE");
		int[] cipher = reorder(fkOutput, IPI);
		System.out.println("\n\nAfter rearranging the Plain text as IP Inverse: ");
		for(int i=0;i<8;i++) {
			System.out.print(cipher[i] + " ");
		}
		
		decryption(cipher);
	}
	
	static void decryption(int[] cipher) {
		System.out.println("\n\n-----------DECRYPTION-----------");
		
		System.out.print("\n>>>>>>>IP");
		int[] temp = reorder(cipher, IP);
		System.out.println("\nAfter rearranging the Cipher text as IP: ");
		for(int i=0;i<8;i++) {
			System.out.print(temp[i] + " ");
		}
		
		int[] left = new int[4]; int[] right = new int[4];
		for(int i=0;i<4;i++) {
			left[i] = temp[i];
			right[i] = temp[i+4];
		}
		
		System.out.print("\n\n>>>>>>>FK-1");
		int[] newLeft = fk(temp, key2, left, right);
		
		System.out.print("\n\n>>>>>>>SW");
		System.out.print("\nAfter swapping left and right: ");
		System.out.print("\nLeft Half:  ");
		for(int i=0;i<4;i++) {
			System.out.print(right[i] + " ");
		}
		System.out.print("\nRight Half: ");
		for(int i=0;i<4;i++) {
			System.out.print(newLeft[i] + " ");
		}
		
		System.out.print("\n\n>>>>>>>FK-2");
		int[] fk2 = fk(temp, key1, right, newLeft);
		
		int[] fkOutput = new int[8];
		for(int i=0;i<4;i++) {
			fkOutput[i] = fk2[i];
			fkOutput[i+4] = newLeft[i];
		}
		System.out.println("\n\nOutput of FK function: ");
		for(int i=0;i<8;i++) {
			System.out.print(fkOutput[i] + " ");
		}
		
		System.out.print("\n\n>>>>>>>IP-INVERSE");
		int[] ptext = reorder(fkOutput, IPI);
		System.out.println("\n\nAfter rearranging the Cipher text as IP Inverse: ");
		for(int i=0;i<8;i++) {
			System.out.print(ptext[i] + " ");
		}
	}
	
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int[] key = new int[10];
		int[] ptext = new int[8];
		
		System.out.println("Enter a 8-bit plain text: ");
		for(int i=0;i<8;i++) {
			ptext[i] = sc.nextInt();
		}
		
		System.out.println("Enter a 10-bit key: ");
		for(int i=0;i<10;i++) {
			key[i] = sc.nextInt();
		}
		
		generateKeys(key);
		encryption(ptext);
	}
}

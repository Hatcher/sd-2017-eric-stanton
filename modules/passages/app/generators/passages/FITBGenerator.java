package generators.passages;


import models.passages.Blank;
import models.passages.Passage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;


public class FITBGenerator {
	public static Passage passage;
	public static List<String> words;
	public static List<Integer> selectedIndices;


	public static void pickPlaceholderBlanksForPassage(Passage p) {
		passage = p;
		words = Passage.getWordsInPassage(passage);
		selectedIndices = new ArrayList<Integer>();

		int i = 0;
		for (String word : words) {
			System.out.print(i + ":" + word + " ");
			i++;
		}

		if (words.size() > 2) {
			pickMultiWordBlanks();
		}
		pickSingleWordBlanks();
	}


	public static void pickSingleWordBlanks() {
		int numBlanks = getRandomNumber(1, Math.max(1, words.size()/5));
		int numSelected = 0;

		while(numSelected < numBlanks) {
			int randomIndex = getRandomNumber(0, words.size()-1);

			if (!selectedIndices.contains(randomIndex)) {
				String word = words.get(randomIndex);

				selectedIndices.add(randomIndex);
				numSelected++;

				System.out.println("CREATING WORD : " + randomIndex + " (" + word + ")");
				Blank blank = new Blank(passage, randomIndex, randomIndex);
				Blank.create(blank);
			}

		}
	}


	public static void pickMultiWordBlanks() {
		int numBlanks = getRandomNumber(2, 5);
		int numSelected = 0;

		while(numSelected < numBlanks) {
			int howManyWordsInPhrase = getRandomNumber(2, 4);
			int startIndex = getRandomNumber(0, words.size());

			String phrase = "";
			int endIndex = -1;

			for (int j = 0; j < howManyWordsInPhrase; j++) {
				int randomIndex = getRandomNumber(0, words.size());

				if (!selectedIndices.contains(randomIndex)) {
					int currentIndex = startIndex + j;
					if (currentIndex > words.size()) {
						break;
					}
					phrase += words.get(currentIndex) + " ";
					endIndex = currentIndex;
				} else {
					phrase = "";
					break;
				}
			}

			if (!Objects.equals(phrase, "")) {
				for (int index = startIndex; index <= endIndex; index++) {
					selectedIndices.add(index);
					numSelected++;
				}

				System.out.println("\nCREATING PHRASE : " + startIndex + "-" + endIndex + " (" + phrase + ")");
				Blank blank = new Blank(passage, startIndex, endIndex);
				Blank.create(blank);
			}
		}
	}


	public static int getRandomNumber(int minimum, int maximum) {
		Random rand = new Random();
		return rand.nextInt((maximum - minimum) + 1) + minimum;
	}

}

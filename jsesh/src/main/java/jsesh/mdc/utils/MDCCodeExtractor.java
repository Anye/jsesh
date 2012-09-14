package jsesh.mdc.utils;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import jsesh.hieroglyphs.CompositeHieroglyphsManager;
import jsesh.hieroglyphs.GardinerCode;
import jsesh.mdc.MDCParserFacade;
import jsesh.mdc.MDCSyntaxError;
import jsesh.mdc.interfaces.HieroglyphInterface;
import jsesh.mdc.interfaces.MDCBuilderAdapter;
import jsesh.mdc.interfaces.ModifierListInterface;

/**
 * An extractor is able to fetch codes from a manuel de codage text, and, if needed to normalize them.
 * @author rosmord
 *
 */
public class MDCCodeExtractor {
	private boolean normalize= true;
	
	private boolean suppressNonGlyphs= true;
	
	public String[] getCodes(String manuelDeCodageText) throws MDCSyntaxError {
		SignListBuilder builder = new SignListBuilder();
		MDCParserFacade parser = new MDCParserFacade(builder);
		parser.parse(new StringReader(manuelDeCodageText));
		return builder.result.toArray(new String[builder.result.size()]);
	}
	

	
	private class SignListBuilder extends MDCBuilderAdapter {
		
		public List<String> result;

		/* (non-Javadoc)
		 * @see jsesh.mdc.interfaces.MDCBuilder#reset()
		 */
		public void reset() {
			result= new LinkedList<String>();
		}
	
		/* (non-Javadoc)
		 * @see jsesh.mdc.interfaces.MDCBuilder#buildHieroglyph(boolean, int, java.lang.String, jsesh.mdc.interfaces.ListInterface, int)
		 */
		public HieroglyphInterface buildHieroglyph(boolean isGrammar, int type, String code, ModifierListInterface m, int isEnd) {
			String actualCode= code;
			if (GardinerCode.isCanonicalCode(code))
				result.add(actualCode);
			else  {
				String canonicalCode = CompositeHieroglyphsManager.getInstance().getCanonicalCode(code);
				if (!suppressNonGlyphs || GardinerCode.isCanonicalCode(canonicalCode))
					result.add(canonicalCode);
			}
			return null;
		}

	}



	/**
	 * Should we normalize the codes to "Gardiner" code ?
	 * @return the normalize
	 */
	public boolean isNormalize() {
		return normalize;
	}



	/**
	 * Should we normalize the codes to "Gardiner" code ?
	 * Note that currently, the codes are always normalized. We are not really
	 * interested in non normalized codes, so if you need the alternative, write it.
	 * @param normalize 
	 */
	public void setNormalize(boolean normalize) {
		this.normalize = normalize;
	}
	

	
	public boolean isSuppressNonGlyphs() {
		return suppressNonGlyphs;
	}


	/**
	 * Should we suppress codes that are not in Gardiner form (for instance codes for parenthesis or dots) ?
	 * @param suppressNonGlyphs
	 */
	public void setSuppressNonGlyphs(boolean suppressNonGlyphs) {
		this.suppressNonGlyphs = suppressNonGlyphs;
	}



	public static void main(String[] args) throws MDCSyntaxError {
		MDCCodeExtractor mDCCodeExtractor= new MDCCodeExtractor();
		String[] l = mDCCodeExtractor.getCodes("i-w-r:a-O-$b-ra-[[-m-]]-p*t:pt-$r");
		for (int i=0; i < l.length; i++)
			System.out.print(l[i]+ " ");
		System.out.println();
	}	
}
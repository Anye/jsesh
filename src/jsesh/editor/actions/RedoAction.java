package jsesh.editor.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import jsesh.editor.JMDCEditor;
import jsesh.swing.KeyUtils;

public class RedoAction extends EditorAction {

	public RedoAction(JMDCEditor editor) {
		super(editor, "redo");
		putValue(ACCELERATOR_KEY, KeyUtils.buildRedoShortCut());

	}
	
	public void actionPerformed(ActionEvent e) {
		if (editor.isEditable())
			editor.getWorkflow().redo();
	}

}

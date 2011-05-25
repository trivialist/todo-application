/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * HTMLEditor.java
 *
 * Created on 18.05.2011, 15:15:44
 */
package todo.gui.htmleditor;

import java.awt.Color;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import net.atlanticbb.tantlinger.ui.text.CompoundUndoManager;
import net.atlanticbb.tantlinger.ui.text.HTMLUtils;
import net.atlanticbb.tantlinger.ui.text.WysiwygHTMLEditorKit;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLBlockAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLInlineAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLTextEditAction;
import org.bushe.swing.action.ActionList;
import org.bushe.swing.action.ActionManager;

/**
 *
 * @author Sven
 */
public class HTMLEditor extends javax.swing.JPanel
{
	private JEditorPane editorPane;
	private static final String INVALID_TAGS[] =
	{
		"html", "head", "body", "title"
	};
	private CaretListener caretHandler = new CaretHandler();
	private ActionList actionList;
	private HTMLTextEditAction boldAction, italicAction, underlineAction, ulListAction, olListAction, colorAction;
	public static Color newColor = null;

	public HTMLEditor()
	{
		initEditor("<p></p>");
	}

	public HTMLEditor(String initialContent)
	{
		initEditor(initialContent);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		editorPane.setEnabled(enabled);
		jButton1.setEnabled(enabled);
		jButton2.setEnabled(enabled);
		jButton3.setEnabled(enabled);
		jButton4.setEnabled(enabled);
		jButton5.setEnabled(enabled);
		jButton6.setEnabled(enabled);
		jButton7.setEnabled(enabled);
		jButton8.setEnabled(enabled);
		jButton9.setEnabled(enabled);
	}

	private void initEditor(String initialContent)
	{
		initComponents();

		actionList = new ActionList("editor-actions");

		editorPane = createWysiwygEditor(initialContent);
		jScrollPane1.setViewportView(editorPane);

		boldAction = new HTMLInlineAction(HTMLInlineAction.BOLD);
		boldAction.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
		actionList.add(boldAction);

		italicAction = new HTMLInlineAction(HTMLInlineAction.ITALIC);
		italicAction.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
		actionList.add(italicAction);

		underlineAction = new HTMLInlineAction(HTMLInlineAction.UNDERLINE);
		underlineAction.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
		actionList.add(underlineAction);

		ulListAction = new HTMLBlockAction(HTMLBlockAction.UL);
		ulListAction.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
		actionList.add(ulListAction);

		olListAction = new HTMLBlockAction(HTMLBlockAction.OL);
		olListAction.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
		actionList.add(olListAction);

		colorAction = new HTMLFontColorActionModified();
        actionList.add(colorAction);
	}

	private class CaretHandler implements CaretListener
	{
		public void caretUpdate(CaretEvent e)
		{
			actionList.putContextValueForAll(HTMLTextEditAction.EDITOR, editorPane);
			actionList.updateEnabledForAll();
		}
	}

	private JEditorPane createWysiwygEditor(String initialContent)
	{
		JEditorPane ed = new JEditorPane();
		ed.setEditorKitForContentType("text/html", new WysiwygHTMLEditorKit());
		ed.setContentType("text/html");

		insertHTML(ed, initialContent, 0);
		ed.addCaretListener(caretHandler);

		return ed;
	}

	public void setText(String text)
	{
		String topText = removeInvalidTags(text);
		editorPane.setText("");
		insertHTML(editorPane, topText, 0);
		CompoundUndoManager.discardAllEdits(editorPane.getDocument());
	}

	public String getText()
	{
		return removeInvalidTags(editorPane.getText());
	}

	private String removeInvalidTags(String html)
	{
		for (int i = 0; i < INVALID_TAGS.length; i++)
		{
			html = deleteOccurance(html, '<' + INVALID_TAGS[i] + '>');
			html = deleteOccurance(html, "</" + INVALID_TAGS[i] + '>');
		}

		return html.trim();
	}

	private String deleteOccurance(String text, String word)
	{
		StringBuilder sb = new StringBuilder(text);
		int p;

		while ((p = sb.toString().toLowerCase().indexOf(word.toLowerCase())) != -1)
		{
			sb.delete(p, p + word.length());
		}

		return sb.toString();
	}

	private void insertHTML(JEditorPane editor, String html, int location)
	{
		try
		{
			HTMLEditorKit kit = (HTMLEditorKit) editor.getEditorKit();
			Document doc = editor.getDocument();
			StringReader reader = new StringReader(HTMLUtils.jEditorPaneizeHTML(html));
			kit.read(reader, doc, location);
		}
		catch (Exception ex)
		{
			Logger.getLogger(HTMLEditor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void executeAction(HTMLTextEditAction action)
	{
		try
		{
			action.execute(null);
		}
		catch (Exception ex)
		{
			Logger.getLogger(HTMLEditor.class.getName()).log(Level.SEVERE, null, ex);
		}

		editorPane.requestFocus();
	}

	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("File");
        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setText("B");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton1.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jButton2.setText("I");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton2.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setText("<html><u>U</u></html>");
        jButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton3.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jSeparator1.setMaximumSize(new java.awt.Dimension(16, 32767));
        jSeparator1.setPreferredSize(new java.awt.Dimension(16, 0));
        jToolBar1.add(jSeparator1);

        jButton4.setText("<html>&#9679;</html>");
        jButton4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton4.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton5.setText("1.");
        jButton5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton5.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jSeparator2.setMaximumSize(new java.awt.Dimension(16, 32767));
        jSeparator2.setPreferredSize(new java.awt.Dimension(16, 0));
        jToolBar1.add(jSeparator2);

        jButton6.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 0, 0));
        jButton6.setText("<html>&#9632;</html>");
        jButton6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setIconTextGap(0);
        jButton6.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton6.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton6.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jButton7.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 204, 0));
        jButton7.setText("<html>&#9632;</html>");
        jButton7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setIconTextGap(0);
        jButton7.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton7.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton7.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        jButton8.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        jButton8.setForeground(new java.awt.Color(0, 0, 255));
        jButton8.setText("<html>&#9632;</html>");
        jButton8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setIconTextGap(0);
        jButton8.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton8.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton8.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton8);

        jButton9.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        jButton9.setText("<html>&#9632;</html>");
        jButton9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setIconTextGap(0);
        jButton9.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton9.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton9.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
	{//GEN-HEADEREND:event_jButton1ActionPerformed
		executeAction(boldAction);
	}//GEN-LAST:event_jButton1ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
	{//GEN-HEADEREND:event_jButton2ActionPerformed
		executeAction(italicAction);
	}//GEN-LAST:event_jButton2ActionPerformed

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
	{//GEN-HEADEREND:event_jButton3ActionPerformed
		executeAction(underlineAction);
	}//GEN-LAST:event_jButton3ActionPerformed

	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
	{//GEN-HEADEREND:event_jButton4ActionPerformed
		executeAction(ulListAction);
	}//GEN-LAST:event_jButton4ActionPerformed

	private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
	{//GEN-HEADEREND:event_jButton5ActionPerformed
		executeAction(olListAction);
	}//GEN-LAST:event_jButton5ActionPerformed

	private void jButton6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton6ActionPerformed
	{//GEN-HEADEREND:event_jButton6ActionPerformed
		newColor = new Color(255, 0, 0);
		executeAction(colorAction);
	}//GEN-LAST:event_jButton6ActionPerformed

	private void jButton7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton7ActionPerformed
	{//GEN-HEADEREND:event_jButton7ActionPerformed
		newColor = new Color(0, 200, 51);
		executeAction(colorAction);
	}//GEN-LAST:event_jButton7ActionPerformed

	private void jButton8ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton8ActionPerformed
	{//GEN-HEADEREND:event_jButton8ActionPerformed
		newColor = new Color(0, 0, 255);
		executeAction(colorAction);
	}//GEN-LAST:event_jButton8ActionPerformed

	private void jButton9ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton9ActionPerformed
	{//GEN-HEADEREND:event_jButton9ActionPerformed
		newColor = new Color(0, 0, 0);
		executeAction(colorAction);
	}//GEN-LAST:event_jButton9ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}

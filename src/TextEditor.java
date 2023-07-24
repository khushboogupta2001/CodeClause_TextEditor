import java.awt.*;
import java.awt.FileDialog;
import java.awt.datatransfer.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.*;

class Myframe extends Frame
{
    Menu file,sub,edit,help;
    MenuItem newItem,open,save,saveas,close,cut,copy,paste;
    CheckboxMenuItem auto;
    TextArea ta;

    Myframe() {
        super("Text Editor");
        newItem=new MenuItem("New");
        open = new MenuItem("Open");
        save =new MenuItem("Save");
        saveas = new MenuItem("Save As");
        close = new MenuItem("Close");
        cut=new MenuItem("Cut");
        cut.setShortcut(new MenuShortcut(KeyEvent.VK_X, false));
        copy=new MenuItem("Copy");
        copy.setShortcut(new MenuShortcut(KeyEvent.VK_C, false));
        paste=new MenuItem("Paste");
        paste.setShortcut(new MenuShortcut(KeyEvent.VK_V,false));

        auto = new CheckboxMenuItem("Auto Save");

        file = new Menu("File");
//        close=new Menu("Close");
        sub = new Menu("Save");
        edit=new Menu("Edit");
        help=new Menu("Help");

        file.add(newItem);
        file.add(open);
        file.add(close);
        file.add(sub);
        file.add(auto);

        sub.add(save);
        sub.add(saveas);

        edit.add(cut);
        edit.add(copy);
        edit.add(paste);

        MenuBar mb = new MenuBar();
        mb.add(file);
        mb.add(edit);
        mb.add(help);
//        mb.add(sub);
        setMenuBar(mb);

        ta = new TextArea();
        setLayout(new BorderLayout());
        add(ta);

        ta.setFont(new Font("Arial", Font.PLAIN, 20));

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });

        saveas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveasFile();
            }
        });

        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newFile();
            }
        });

        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cut();
            }
        });

        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy();
            }
        });

        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paste();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
        public void openFile() {
        FileDialog openDialogBox = new FileDialog(this, "Open", FileDialog.LOAD);
        openDialogBox.setVisible(true);

        String selectedFile = openDialogBox.getDirectory() + openDialogBox.getFile();
        Path filePath = Paths.get(selectedFile);
        try {
            String content = Files.readString(filePath);
            ta.setText(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveFile() {
        String selectedFile = null;
        FileDialog saveDialog = new FileDialog(this, "Save", FileDialog.SAVE);
//        saveDialog.setVisible(true);

        if (saveDialog.getFile() != null) {
            selectedFile = saveDialog.getDirectory() + saveDialog.getFile();
        }

        if (selectedFile != null) {
            Path filePath = Paths.get(selectedFile);
            try {
                String content = ta.getText();
                Files.write(filePath, content.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            saveasFile();
        }
    }

    public void saveasFile() {
        FileDialog saveDialog = new FileDialog(this, "Save", FileDialog.SAVE);
        saveDialog.setVisible(true);

        String selectedFile = saveDialog.getDirectory() + saveDialog.getFile();
        Path filePath = Paths.get(selectedFile);
        try {
            String content = ta.getText();
            Files.write(filePath, content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newFile() {
        FileDialog saveDialog = new FileDialog(this, "New", FileDialog.SAVE);
        saveDialog.setVisible(true);

        String selectedFile = saveDialog.getDirectory() + saveDialog.getFile();
        Path filePath = Paths.get(selectedFile);
        try {
            Files.createFile(filePath);  // Create a new file
            ta.setText("");  // Clear the text area
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void cut()
    {
        String selectedText = ta.getSelectedText();
        if (selectedText != null) {
            Clipboard cl = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection select = new StringSelection(selectedText);
            cl.setContents(select, null);
            ta.replaceRange("", ta.getSelectionStart(), ta.getSelectionEnd());
        }
    }

    public void copy()
    {
        String cpText = ta.getSelectedText();
        if (cpText != null && !cpText.isEmpty()) {
            StringSelection select = new StringSelection(cpText);
            Clipboard cl = Toolkit.getDefaultToolkit().getSystemClipboard();
            cl.setContents(select, null);
        }
    }

    public void paste()
    {
        Clipboard cl = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tf = cl.getContents(null);
        if (tf != null && tf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                String pastedText = (String) tf.getTransferData(DataFlavor.stringFlavor);
                int caretPosition = ta.getCaretPosition();
                ta.insert(pastedText, caretPosition);
            } catch (UnsupportedFlavorException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

public class TextEditor
{
    public static void main(String[] args) {
        Myframe f=new Myframe();
        f.setSize(800,700);
        f.setVisible(true);
    }
}
    


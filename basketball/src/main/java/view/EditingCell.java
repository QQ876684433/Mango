package view;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import model.ParamTuple;

/**
 * 实现逻辑，使table中的单元可即刻修改的类
 * 该类目前使用，目前使用javafx内置类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/5/26
 */
public class EditingCell extends TextFieldTableCell<ParamTuple, String> {

    private TextField textField;

    public EditingCell() {
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText((String) getItem());
        setGraphic(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(null);
            }
        }
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> arg0,
                 Boolean arg1, Boolean arg2) -> {
                    if (!arg2) {
                        commitEdit(textField.getText());
                    }
                });
        textField.setOnAction(event -> {
            commitEdit(textField.getText());
        });
        textField.setOnKeyReleased(t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                t.consume();
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}

package com.oni.donjon.stage;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.oni.donjon.action.Actions;

/**
 * @author Daniel Chesters (on 01/06/14).
 */
public class UIStage {

    private Stage stage;
    private List<Actions> actionList;
    private Label messageLabel;

    public UIStage() {
        stage = new Stage();
    }

    public Stage getStage() {
        return stage;
    }

    public List<Actions> getActionList() {
        return actionList;
    }

    public void setActionList(List<Actions> actionList) {
        this.actionList = actionList;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }

    public void setMessageLabel(Label messageLabel) {
        this.messageLabel = messageLabel;
    }
}

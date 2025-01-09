package tictactoe.client.animation;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animation {
    public static ScaleTransition scaleAnimation(Node node, int cycleCount, double scale){
        ScaleTransition transition = new ScaleTransition();
        transition.setNode(node);
        transition.setDuration(Duration.millis(1000));
        transition.setCycleCount(cycleCount);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setByX(scale);
        transition.setByY(scale);
        transition.setAutoReverse(true);
        transition.play();
        return transition;
    } 
}

package com.zenith.frontend;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Onboarding1Controller {

    @FXML private ImageView  centerImage;
    @FXML private Label      headingLine1;
    @FXML private Label      headingLine2;
    @FXML private Label      subtitle;
    @FXML private Button     nextButton;
    @FXML private Button     skipButton;
    @FXML private Rectangle  dot0;
    @FXML private Rectangle  dot1;
    @FXML private Rectangle  dot2;
    @FXML private Rectangle  dot3;

    private int currentPage = 0;

    private static final Color ACTIVE_COLOR   = Color.web("#7dd8f0");
    private static final Color INACTIVE_COLOR = Color.web("#3a6070");
    private static final double PILL_WIDTH    = 28.0;
    private static final double DOT_WIDTH     = 8.0;

    private static final String[] IMAGES = {
        "/images/meditation.png",
        "/images/writing.png",
        "/images/lying.png",
        "/images/consulting.png"
    };
    private static final String[] HEADING1 = {
        "Manage Your Stress",
        "Track Your Mental",
        "Relax Anytime,",
        "Professional Support,"
    };
    private static final String[] HEADING2 = {
        "With Ease",
        "Wellness Daily",
        "Anywhere",
        "When You Need It"
    };
    private static final String[] SUBTITLES = {
        "Your Personal Companion For Clarity",
        "Simple questionnaire to understand\nyour stress level",
        "Meditation, breathing, calming sound,\nand more",
        "Connect with psychologist\nand wellness expert"
    };

    private Rectangle[] dots;

    @FXML
    private void initialize() {
        dots = new Rectangle[]{ dot0, dot1, dot2, dot3 };
    }

    @FXML
    private void handleNext() {
        if (currentPage >= IMAGES.length - 1) {
            SceneNavigator.navigateTo("/login.fxml");
            return;
        }

        nextButton.setDisable(true);

        int nextPage = currentPage + 1;
        Image nextImage = new Image(getClass().getResourceAsStream(IMAGES[nextPage]));

        FadeTransition  imgFadeOut  = fade(centerImage,  Duration.millis(220), 1, 0);
        ScaleTransition imgScaleOut = scale(centerImage, Duration.millis(220), 1.0, 0.88);
        imgScaleOut.setInterpolator(Interpolator.EASE_IN);

        ParallelTransition phaseA = new ParallelTransition(
            new ParallelTransition(imgFadeOut, imgScaleOut),
            textFadeSlide(headingLine1, -10, true),
            textFadeSlide(headingLine2, -10, true),
            textFadeSlide(subtitle,     -8,  true)
        );

        FadeTransition  imgFadeIn  = fade(centerImage,  Duration.millis(380), 0, 1);
        ScaleTransition imgScaleIn = scale(centerImage, Duration.millis(380), 0.88, 1.0);
        imgScaleIn.setInterpolator(Interpolator.EASE_OUT);

        Timeline dotMorph = buildDotMorph(currentPage, nextPage);

        ParallelTransition phaseB = new ParallelTransition(
            new ParallelTransition(imgFadeIn, imgScaleIn),
            textFadeSlide(headingLine1, 14, false),
            textFadeSlide(headingLine2, 14, false),
            textFadeSlide(subtitle,     10, false),
            dotMorph
        );

        SequentialTransition seq = new SequentialTransition(phaseA, phaseB);

        phaseA.setOnFinished(e -> {
            centerImage.setImage(nextImage);
            centerImage.setScaleX(0.88);
            centerImage.setScaleY(0.88);

            headingLine1.setText(HEADING1[nextPage]);
            headingLine2.setText(HEADING2[nextPage]);
            subtitle.setText(SUBTITLES[nextPage]);

            // prime slide-in start position
            headingLine1.setTranslateY(14);
            headingLine2.setTranslateY(14);
            subtitle.setTranslateY(10);
        });

        seq.setOnFinished(e -> {
            currentPage = nextPage;
            nextButton.setDisable(false);
            if (currentPage == IMAGES.length - 1) {
                nextButton.setText("Get Started  ›");
            }
        });

        seq.play();
    }

    @FXML
    private void handleSkip() {
        SceneNavigator.navigateTo("/login.fxml");
    }

    private FadeTransition fade(javafx.scene.Node node, Duration dur, double from, double to) {
        FadeTransition ft = new FadeTransition(dur, node);
        ft.setFromValue(from);
        ft.setToValue(to);
        return ft;
    }

    private ScaleTransition scale(javafx.scene.Node node, Duration dur, double from, double to) {
        ScaleTransition st = new ScaleTransition(dur, node);
        st.setFromX(from); st.setFromY(from);
        st.setToX(to);     st.setToY(to);
        return st;
    }

    private ParallelTransition textFadeSlide(javafx.scene.Node node, double byY, boolean out) {
        Duration dur = Duration.millis(out ? 180 : 300);

        FadeTransition ft = new FadeTransition(dur, node);
        ft.setFromValue(out ? 1 : 0);
        ft.setToValue(out ? 0 : 1);
        ft.setInterpolator(Interpolator.EASE_BOTH);

        TranslateTransition tt = new TranslateTransition(dur, node);
        if (out) {
            tt.setByY(byY);
        } else {
            tt.setFromY(byY);
            tt.setToY(0);
        }
        tt.setInterpolator(Interpolator.EASE_OUT);

        return new ParallelTransition(ft, tt);
    }

    private Timeline buildDotMorph(int fromPage, int toPage) {
        Rectangle fromDot = dots[fromPage];
        Rectangle toDot   = dots[toPage];

        return new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(fromDot.widthProperty(), PILL_WIDTH),
                new KeyValue(fromDot.fillProperty(),  ACTIVE_COLOR)
            ),
            new KeyFrame(Duration.millis(320),
                new KeyValue(fromDot.widthProperty(), DOT_WIDTH,     Interpolator.EASE_BOTH),
                new KeyValue(fromDot.fillProperty(),  INACTIVE_COLOR, Interpolator.EASE_BOTH)
            ),
            new KeyFrame(Duration.ZERO,
                new KeyValue(toDot.widthProperty(), DOT_WIDTH),
                new KeyValue(toDot.fillProperty(),  INACTIVE_COLOR)
            ),
            new KeyFrame(Duration.millis(320),
                new KeyValue(toDot.widthProperty(), PILL_WIDTH,   Interpolator.EASE_BOTH),
                new KeyValue(toDot.fillProperty(),  ACTIVE_COLOR, Interpolator.EASE_BOTH)
            )
        );
    }
}

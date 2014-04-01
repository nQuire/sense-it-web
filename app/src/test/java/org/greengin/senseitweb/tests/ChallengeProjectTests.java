package org.greengin.senseitweb.tests;


import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivityStage;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityActions;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityRequest;
import org.greengin.senseitweb.logic.project.challenge.ChallengeAnswerRequest;
import org.greengin.senseitweb.logic.project.challenge.NewChallengeAnswerResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet-tests.xml")
public class ChallengeProjectTests extends AbstractProjectTests {




    public ChallengeProjectTests() {
        super("challenge", ProjectType.CHALLENGE);
    }


    private ChallengeActivityActions challengeActions(UserProfile user) {
        return new ChallengeActivityActions(projectId, subscriptionManager, fileManager, user, true, entityManagerFactory.createEntityManager());
    }

    private ChallengeActivity activity() {
        return (ChallengeActivity) helper.getProject(projectId).getActivity();
    }

    private void setMaxQuestions(UserProfile user, int max) {
        ChallengeActivityRequest request = new ChallengeActivityRequest();
        request.setMaxAnswers(max);
        challengeActions(user).updateActivity(request);
    }



    @Test
    public void testDefaultMaxQuestions() {
        Assert.assertEquals(1, ChallengeActivity.DEFAULT_MAX);
    }

    @Test
    public void testMaxQuestions() {
        super.testProjectEdition(new ProjectManipulator() {
            @Override
            public void modify(UserProfile user) {
                setMaxQuestions(user, 2);
            }

            @Override
            public void reset(UserProfile user) {
                setMaxQuestions(user, ChallengeActivity.DEFAULT_MAX);
            }

            @Override
            public void testModified() {
                Assert.assertEquals(2, activity().getMaxAnswers().longValue());
            }

            @Override
            public void testSame() {
                Assert.assertEquals(ChallengeActivity.DEFAULT_MAX, activity().getMaxAnswers().longValue());
            }
        });
    }


    @Test
    public void testDuringProposal() {
        projectActions(author).setOpen(true);
        Assert.assertEquals(activity().getStage(), ChallengeActivityStage.PROPOSAL);

        final ChallengeAnswerRequest request = new ChallengeAnswerRequest();

        super.testPrivateMemberAction(new ProjectManipulator() {
            Long answerId;

            @Override
            public void modify(UserProfile user) {
                NewChallengeAnswerResponse answer = challengeActions(user).createAnswer(request);
                answerId = answer != null ? answer.getNewAnswer() : null;
            }

            @Override
            public void reset(UserProfile user) {
                challengeActions(user).deleteAnswer(answerId);
            }

            @Override
            public void testModified() {
                if (project().getOpen()) {
                    Assert.assertEquals(1, challengeActions(member).getAnswersForParticipant().size());
                    Assert.assertEquals(0, challengeActions(member2).getAnswersForParticipant().size());
                } else {
                    Assert.assertNull(challengeActions(member).getAnswersForParticipant());
                }
            }

            @Override
            public void testSame() {
                if (project().getOpen()) {
                    Assert.assertEquals(0, challengeActions(member).getAnswersForParticipant().size());
                    Assert.assertEquals(0, challengeActions(member2).getAnswersForParticipant().size());
                } else {
                    Assert.assertNull(challengeActions(member).getAnswersForParticipant());
                }
            }
        });
    }

}

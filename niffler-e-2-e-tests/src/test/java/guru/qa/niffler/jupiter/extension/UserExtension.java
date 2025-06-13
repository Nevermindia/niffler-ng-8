package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.UsersApiClient;
import guru.qa.niffler.test.web.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class UserExtension implements BeforeEachCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private static final String defaultPassword = "12345";
    private final UsersClient usersClient = new UsersApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if("".equals(anno.username())){
                        final String username = RandomDataUtils.randomUsername();

                        UserJson user = usersClient.createUser(username, defaultPassword);
                        usersClient.addIncomeInvitation(user, anno.incomeInvitations());
                        usersClient.addOutcomeInvitation(user, anno.outcomeInvitations());
                        usersClient.addFriend(user, anno.friends());
                        setUser(user);
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getUserJson();
    }

    public static UserJson getUserJson(){
        final ExtensionContext context = TestMethodContextExtension.context();
        return context.getStore(NAMESPACE).get(context.getUniqueId(), UserJson.class);
    }

    public static void setUser(UserJson user) {
        final ExtensionContext context = TestMethodContextExtension.context();
        context.getStore(NAMESPACE)
                .put(context.getUniqueId(), user.withPassword(defaultPassword));
    }
}

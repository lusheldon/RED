package org.robotframework.ide.eclipse.main.plugin.project.editor;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.StylersDisposingLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.robotframework.ide.eclipse.main.plugin.RedTheme;
import org.robotframework.ide.eclipse.main.plugin.project.RobotProjectConfig.ReferencedVariableFile;

import com.google.common.base.Joiner;

class VariableFilesLabelProvider extends StylersDisposingLabelProvider {
    
    private String projectPath;
    
    public VariableFilesLabelProvider(final String projectPath) {
        this.projectPath = projectPath;
    }

    @Override
    public StyledString getStyledText(final Object element) {
        final ReferencedVariableFile varFile = (ReferencedVariableFile) element;

        final StyledString label = new StyledString(varFile.getName());
        final List<String> args = varFile.getArguments();
        String argString = "";
        if (args != null && !args.isEmpty()) {
            argString += ":" + Joiner.on(":").join(args);
        }
        final IPath path = new Path(varFile.getPath());
        final String parentPath = path.segmentCount() > 1 ? path.removeLastSegments(1).toString() : projectPath;
        label.append(argString + " - " + parentPath, new Styler() {

            @Override
            public void applyStyles(final TextStyle textStyle) {
                textStyle.foreground = RedTheme.getEclipseDecorationColor();
            }
        });
        return label;

    }

    @Override
    public Image getImage(final Object element) {
        return null;
    }
}

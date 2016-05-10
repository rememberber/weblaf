/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.painter.decoration.border;

import com.alee.api.ColorSupport;
import com.alee.api.StrokeSupport;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Simple line border implementation.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> border type
 * @author Mikle Garin
 */

@XStreamAlias ("LineBorder")
public class LineBorder<E extends JComponent, D extends IDecoration<E, D>, I extends LineBorder<E, D, I>> extends AbstractBorder<E, D, I>
        implements ColorSupport, StrokeSupport
{
    /**
     * Shade width.
     */
    @XStreamAsAttribute
    protected Stroke stroke;

    /**
     * Shade color.
     */
    @XStreamAsAttribute
    protected Color color;

    @Override
    public Stroke getStroke ()
    {
        return stroke;
    }

    @Override
    public Color getColor ()
    {
        return color != null ? color : new Color ( 210, 210, 210 );
    }

    @Override
    public BorderWidth getWidth ()
    {
        final float t = getOpacity ();
        final Stroke s = getStroke ();
        final float w = t > 0 ? s != null && s instanceof BasicStroke ? ( ( BasicStroke ) s ).getLineWidth () : 1 : 0;
        final int bw = Math.round ( w );
        return new BorderWidth ( bw, bw, bw, bw );
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d, final Shape shape )
    {
        final float opacity = getOpacity ();
        if ( opacity > 0 && !getWidth ().isEmpty () )
        {
            final Stroke stroke = getStroke ();
            final Color color = getColor ();

            final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, opacity, opacity < 1f );
            final Stroke os = GraphicsUtils.setupStroke ( g2d, stroke, stroke != null );
            final Paint op = GraphicsUtils.setupPaint ( g2d, color, color != null );

            g2d.draw ( shape );

            GraphicsUtils.restorePaint ( g2d, op, color != null );
            GraphicsUtils.restoreStroke ( g2d, os, stroke != null );
            GraphicsUtils.restoreComposite ( g2d, oc, opacity < 1f );
        }
    }

    @Override
    public I merge ( final I border )
    {
        super.merge ( border );
        if ( border.stroke != null )
        {
            stroke = border.stroke;
        }
        if ( border.color != null )
        {
            color = border.color;
        }
        return ( I ) this;
    }
}
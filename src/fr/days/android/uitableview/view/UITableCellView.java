package fr.days.android.uitableview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.days.android.uitableview.adapter.UITableViewInternalAccessoryListener;
import fr.days.android.uitableview.drawable.UITableCellDrawable;
import fr.days.android.uitableview.model.AccessoryType;
import fr.days.android.uitableview.model.IndexPath;
import gws.grottworkshop.gwsbluedroid.R;

public class UITableCellView extends UITableItemView {

	private static final int INSET = 10;

	private static int[] colorLineDefault;
	private static int[] colorLinePressed;
	private static int borderColor = Integer.MIN_VALUE;

	private ImageView imageView;
	private TextView titleView;
	private TextView subtitleView;
	private ImageView accessoryView;
	private UITableViewInternalAccessoryListener internalAccessoryListener;

	public UITableCellView(Context context, IndexPath indexPath) {
		super(context, indexPath);

		if (borderColor == Integer.MIN_VALUE) {
			borderColor = getColor(R.color.cell_border);
			colorLineDefault = new int[] { getResources().getColor(R.color.base_start_color_line_default), getResources().getColor(R.color.base_end_color_line_default) };
			colorLinePressed = new int[] { getResources().getColor(R.color.base_start_color_line_pressed), getResources().getColor(R.color.base_end_color_line_pressed) };
		}

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (inflater != null) {
			inflater.inflate(R.layout.uitv_table_cell, this);
		}

		imageView = (ImageView) findViewById(R.id.image);
		titleView = (TextView) findViewById(R.id.title);
		subtitleView = (TextView) findViewById(R.id.subtitle);
		accessoryView = (ImageView) findViewById(R.id.accessory);

		// Set default color
		setDefaultBackgroundColor();

		// Increase the touchable area for accessoryView
		post(getTouchDelegateAction(this, accessoryView, 30, 30, 30, 30));
	}

	public UITableCellView(Context context, IndexPath indexPath, String title, String subtitle) {
		this(context, indexPath);
		setTitle(title);
		setSubtitle(subtitle);
	}

	private int getColor(int colorId) {
		return getResources().getColor(colorId);
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImage(Integer imageResource) {
		if (imageResource == null) {
			imageView.setVisibility(View.GONE);
		} else {
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageResource(imageResource);
		}
	}

	public void setImage(Drawable drawable) {
		if (drawable == null) {
			imageView.setVisibility(View.GONE);
		} else {
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageDrawable(drawable);
		}
	}

	public void setImageBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			imageView.setVisibility(View.GONE);
		} else {
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageBitmap(bitmap);
		}
	}

	public TextView getTitleView() {
		return titleView;
	}

	public String getTitle() {
		return titleView.getText().toString();
	}

	public void setTitle(String title) {
		this.titleView.setText(title);
	}

	public TextView getSubtitleView() {
		return subtitleView;
	}

	public String getSubtitle() {
		return subtitleView.getText().toString();
	}

	public void setSubtitle(String subtitle) {
		if (TextUtils.isEmpty(subtitle)) {
			subtitleView.setVisibility(View.GONE);
		} else {
			subtitleView.setVisibility(View.VISIBLE);
			subtitleView.setText(subtitle);
		}
	}

	public ImageView getAccessoryView() {
		return accessoryView;
	}

	public void setAccessory(AccessoryType accessoryType) {
		if (accessoryType == AccessoryType.NONE) {
			accessoryView.setVisibility(View.GONE);
		} else {
			accessoryView.setVisibility(View.VISIBLE);

			switch (accessoryType) {
			case DISCLOSURE:
				accessoryView.setImageResource(R.drawable.uitv_accessory_disclosure);
				break;
			}
		}
	}

	public void setAccessory(Drawable drawable) {
		if (drawable == null) {
			accessoryView.setVisibility(View.GONE);
		} else {
			accessoryView.setVisibility(View.VISIBLE);
			accessoryView.setImageDrawable(drawable);
		}
	}

	public void setDefaultBackgroundColor() {
		setBackgroundColor(colorLineDefault, colorLinePressed);
	}

	public void setBackgroundColor(int[] colorDefault, int[] colorPressed) {
		// Assign the right backgroundDrawable according to the cell's position in the group
		Drawable backgroundDrawable;
		if (indexPath.getRowsCount() == 1) {
			backgroundDrawable = new UITableCellDrawable(10.0f, 10.0f, colorDefault, colorPressed, borderColor);
		} else {
			if (indexPath.isFirstCellOfGroup()) {
				backgroundDrawable = new UITableCellDrawable(10.0f, 0, colorDefault, colorPressed, borderColor);
			} else if (indexPath.isLastCellOfGroup()) {
				backgroundDrawable = new UITableCellDrawable(0, 10.0f, colorDefault, colorPressed, borderColor);
			} else {
				backgroundDrawable = new UITableCellDrawable(0, 0, colorDefault, colorPressed, borderColor);
			}
		}

		// Add extra space if this cell is the last one
		int bottomInset = 0;
		if (indexPath.isLastCell()) {
			bottomInset = INSET;
		}
		setBackgroundDrawable(new InsetDrawable(backgroundDrawable, INSET, 0, INSET, bottomInset));
	}

	public UITableViewInternalAccessoryListener getInternalAccessoryListener() {
		return internalAccessoryListener;
	}

	public void setInternalAccessoryListener(final UITableViewInternalAccessoryListener internalAccessoryListener) {
		if (internalAccessoryListener != null) {
			accessoryView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					internalAccessoryListener.onCellAccessoryClick(UITableCellView.this.indexPath);
				}
			});
			accessoryView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					return internalAccessoryListener.onCellAccessoryLongClick(UITableCellView.this.indexPath);
				}
			});
		}

		this.internalAccessoryListener = internalAccessoryListener;
	}

	@Override
	public void setIndexPath(IndexPath indexPath) {
		super.setIndexPath(indexPath);
		setBackgroundColor(colorLineDefault, colorLinePressed);
	}

	/**
	 * Adds a touchable padding around a View by constructing a TouchDelegate and adding it to parent View.
	 * 
	 * @param parent
	 *            The "outer" parent View
	 * @param delegate
	 *            The delegate that handles the TouchEvents
	 * @param topPadding
	 *            Additional touch area in pixels above View
	 * @param bootomPadding
	 *            Additional touch area in pixels below View
	 * @param topPadding
	 *            Additional touch area in pixels left to View
	 * @param topPadding
	 *            Additional touch area in pixels right to View
	 * @return A runnable that you can post as action to a Views event queue
	 */
	private static Runnable getTouchDelegateAction(final View parent, final View delegate, final int topPadding, final int bottomPadding, final int leftPadding, final int rightPadding) {
		return new Runnable() {
			@Override
			public void run() {

				// Construct a new Rectangle and let the Delegate set its values
				Rect touchRect = new Rect();
				delegate.getHitRect(touchRect);

				// Modify the dimensions of the Rectangle
				// Padding values below zero are replaced by zeros
				touchRect.top -= Math.max(0, topPadding);
				touchRect.bottom += Math.max(0, bottomPadding);
				touchRect.left -= Math.max(0, leftPadding);
				touchRect.right += Math.max(0, rightPadding);

				// Now we are going to construct the TouchDelegate
				TouchDelegate touchDelegate = new TouchDelegate(touchRect, delegate);

				// And set it on the parent
				parent.setTouchDelegate(touchDelegate);

			}
		};
	}

}

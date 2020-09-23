/**
 * Copyright (C) 2020 Photon Vision.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package frc.robot;

import org.photonvision.Packet;
import org.photonvision.PhotonPipelineResult;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Represents a camera that is connected to PhotonVision.
 */
public class PhotonCamera2 {
  private final NetworkTableEntry rawBytesEntry;
  private final NetworkTableEntry driverModeEntry;
  private final NetworkTableEntry pipelineIndexEntry;

  private boolean driverMode;
  private int pipelineIndex;

  private Packet packet = new Packet(0);

  /**
   * Constructs a PhotonCamera from a root table.
   *
   * @param rootTable The root table that the camera is broadcasting information
   *                  over.
   */
  public PhotonCamera2(NetworkTable rootTable) {
    rawBytesEntry = rootTable.getEntry("rawBytes");
    driverModeEntry = rootTable.getEntry("driverMode");
    pipelineIndexEntry = rootTable.getEntry("pipelineIndex");

    driverMode = driverModeEntry.getBoolean(false);
    pipelineIndex = pipelineIndexEntry.getNumber(0).intValue();
  }

  /**
   * Constructs a PhotonCamera from the name of the camera.
   *
   * @param cameraName The nickname of the camera (found in the PhotonVision
   *                   UI).
   */
  public PhotonCamera2(String cameraName) {
    this(NetworkTableInstance.getDefault().getTable("photonvision").getSubTable(cameraName));
  }

  /**
   * Returns the latest pipeline result.
   *
   * @return The latest pipeline result.
   */
  public PhotonPipelineResult getLatestResult() {
    // Clear the packet.
    packet.clear();

    // Create latest result.
    var ret = new PhotonPipelineResult();

    // Populate packet and create result.
    try {
    packet.setData(rawBytesEntry.getRaw(new byte[]{}));
    ret.createFromPacket(packet);
    } catch (Exception e) {}

    // Return result.
    return ret;
  }

  /**
   * Returns whether the camera is in driver mode.
   * @return Whether the camera is in driver mode.
   */
  public boolean getDriverMode() {
    return driverMode;
  }

  /**
   * Toggles driver mode.
   * @param driverMode Whether to set driver mode.
   */
  public void setDriverMode(boolean driverMode) {
    this.driverMode = driverMode;
    driverModeEntry.setBoolean(this.driverMode);
  }

  /**
   * Returns the active pipeline index.
   *
   * @return The active pipeline index.
   */
  public int getPipelineIndex() {
    return pipelineIndex;
  }

  /**
   * Allows the user to select the active pipeline index.
   * @param index The active pipeline index.
   */
  public void setPipelineIndex(int index) {
    pipelineIndex = index;
    pipelineIndexEntry.setNumber(pipelineIndex);
  }

  /**
   * Returns whether the latest target result has targets.
   *
   * @return Whether the latest target result has targets.
   */
  public boolean hasTargets() {
    return getLatestResult().hasTargets();
  }

  /**
   * Returns the pitch of the best target. The best target is defined in the
   * PhotonVision UI.
   *
   * @return The pitch of the best target.
   */
  public double getBestTargetPitch() {
    return getLatestResult().getBestTarget().getPitch();
  }

  /**
   * Returns the yaw of the best target. The best target is defined in the
   * PhotonVision UI.
   *
   * @return The yaw of the best target.
   */
  public double getBestTargetYaw() {
    return getLatestResult().getBestTarget().getYaw();
  }

  /**
   * Returns the area of the best target (0-100). The best target is defined in
   * the PhotonVision UI.
   *
   * @return The area of the best target.
   */
  public double getBestTargetArea() {
    return getLatestResult().getBestTarget().getArea();
  }

  /**
   * Returns the skew of the best target (counter-clockwise positive). The best
   * target is defined in the PhotonVision UI.
   *
   * @return The skew of the best target.
   */
  public double getBestTargetSkew() {
    return getLatestResult().getBestTarget().getSkew();
  }
}

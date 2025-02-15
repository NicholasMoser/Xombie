package com.github.nicholasmoser.xom.ctnr;

public enum XType {
    BaseWeaponContainer,
    BrickBuildingCtr,
    BrickBuildingList,
    BrickLinkage,
    BuildingGlobalContainer,
    BuildingSpecificContainer,
    Campaign,
    CampaignCollective,
    CampaignData,
    ChatMessagesWindowDetails,
    DetailEntityStore,
    EffectDetailsContainer,
    FlyingPayloadWeaponPropertiesContainer,
    GSProfile,
    GSProfileList,
    GunWeaponPropertiesContainer,
    HighScoreData,
    HomingPayloadWeaponPropertiesContainer,
    JumpingPayloadWeaponPropertiesContainer,
    LandFrameStore,
    LevelLock,
    LockedBitmapArray,
    LockedContainer,
    MeleeWeaponPropertiesContainer,
    MenuDescription,
    MineFactoryContainer,
    MovieLock,
    PC_LandChunk,
    PC_LandFrame,
    ParticleEmitterContainer,
    PayloadWeaponPropertiesContainer,
    SchemeColective,
    SchemeCollective,
    SentryGunWeaponPropertiesContainer,
    SoundBankCollective,
    StoredTeamData,
    StringStack,
    TeamDataColective,
    TeamDataCollective,
    W3DTemplateSet,
    WeaponSettingsData,
    WormapediaCollective,
    XAlphaTest,
    XAnimChannel,
    XAnimClipLibrary,
    XAnimInfo,
    XBinModifier,
    XBinSelector,
    XBinormal3fSet,
    XBitmapDescriptor,
    XBlendModeGL,
    XBone,
    XBrickDetails,
    XBrickGeometry,
    XBrickIndexSet,
    XBuildingShape,
    XChildSelector,
    XCollisionData,
    XCollisionGeometry,
    XColor4ubSet,
    XColorResourceDetails,
    XConstColorSet,
    XContainerResourceDetails,
    XCoord3fSet,
    XCoord3sSet_1uScale,
    XCullFace,
    XCustomDescriptor,
    XCustomShader,
    XDataBank,
    XDepthTest,
    XDetailObjectsData,
    XDetailSwitch,
    XDirectMusicDescriptor,
    XEnvironmentMapShader,
    XExpandedAnimInfo,
    XExportAttributeString,
    XFloatResourceDetails,
    XFortsExportedData,
    XGraphSet,
    XGroup,
    XImage,
    XIndexSet,
    XIndexSet8,
    XIndexedCustomTriangleSet,
    XIndexedCustomTriangleStripSet,
    XIndexedTriangleSet,
    XIndexedTriangleStripSet,
    XIntResourceDetails,
    XInteriorNode,
    XInternalSampleData,
    XJointTransform,
    XLightingEnable,
    XMaterial,
    XMatrix,
    XMeshDescriptor,
    XMultiIndexSet,
    XMultiTexCoordSet,
    XMultiTexShader,
    XNormal3fSet,
    XNormal3sSet_1uScale,
    XNullDescriptor,
    XOglTextureMap,
    XPalette,
    XPaletteWeightSet,
    XPathFinderData,
    XPfxEmitterGeom,
    XPointLight,
    XPositionData,
    XPsGeoSet,
    XPsProxyTexture,
    XPsShaderInstance,
    XPsShape,
    XPsSkinShape,
    XPsTexFont,
    XPsTextureReference,
    XSampleData,
    XSceneCamera,
    XSceneryEffectData,
    XShape,
    XSimpleShader,
    XSkeletonRoot,
    XSkin,
    XSkinShape,
    XSlGeoSet,
    XSoundBank,
    XSpriteSet,
    XSpriteSetDescriptor,
    XStreamData,
    XStringResourceDetails,
    XTexCoord2fSet,
    XTexFont,
    XTextDescriptor,
    XTexturePlacement2D,
    XTransform,
    XUintResourceDetails,
    XVectorResourceDetails,
    XWeightSet,
    XXomInfoNode,
    XZBufferWriteEnable;

    public static XType get(String value) {
        try {
            return XType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
